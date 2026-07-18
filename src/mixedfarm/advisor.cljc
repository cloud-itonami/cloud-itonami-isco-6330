(ns mixedfarm.advisor
  "Farm Operations Advisor — the advisor named in this repository's
  README, proposing a farm record-keeping/logistics-coordination
  operation (log a planting/harvest/feeding-schedule/condition-check-in
  record, schedule a household labor/task operation, flag a crop-
  failure/animal-welfare/food-security concern for human review, or
  coordinate a seeds/feed/tools procurement order) from a farmer's
  intake queue, holding roster and supply policy. Swappable mock/llm;
  the advisor ONLY proposes — `mixedfarm.governor` checks farmer/
  holding verification and scope independently and always escalates
  livelihood-concern flags and above-threshold supply orders. Modeled
  on cloud-itonami-isco-6320's husbandry.advisor and
  cloud-itonami-isco-6310's subsistencefarm.advisor.

  This advisor NEVER proposes finalizing a planting/harvest-timing
  agronomic decision, an animal-treatment/welfare/breeding decision, or
  overriding the farmer's own judgment about their household's crops
  or livestock — no such op exists anywhere in the closed allowlist
  below (`mixedfarm.governor/closed-op-allowlist`), and the rationale
  text this advisor emits never uses a finalization/execution phrase
  for any of those actions (`mixedfarm.governor/scope-excluded-terms`),
  so the advisor's own DEFAULT proposals never self-trip the governor's
  scope-exclusion check (see `mixedfarm.governor-test/
  default-mock-advisor-proposals-never-self-trip-scope-exclusion`).
  Any observation suggesting a crop-failure, animal-welfare, injury-
  risk, or food-security concern is surfaced ONLY via
  `:flag-livelihood-concern`, which always escalates to a human and
  never auto-commits — the robot's role ends at \"here is the planting/
  harvest/feeding/roster/condition-check-in status\", never \"here is
  whether to plant or harvest now\" or \"here is whether the animal
  needs treatment or should be bred\". This actor coordinates farm
  record-keeping/logistics ONLY — it never makes the agronomic or
  animal-treatment decision itself.

  A proposal:
  {:op :log-work-record|:schedule-farm-operation|
       :flag-livelihood-concern|:coordinate-supply-order
   :effect :propose :farmer-id str :holding-id (str or nil, only nil
   for :flag-livelihood-concern) :stake kw :confidence n :rationale str,
   plus op-specific fields (:work-id/:work-type/:detail/:timestamp for
   log-work-record; :task-id/:proposed-time/:assignee for
   schedule-farm-operation; :concern-type/:note for
   flag-livelihood-concern; :item/:cost/:vendor for
   coordinate-supply-order)}"
  (:require [clojure.edn :as edn]))

(defprotocol Advisor
  (-advise [advisor store request] "request -> proposal map"))

(defn- rationale-for [op holding-id]
  (str "documented " (name op)
       (if holding-id (str " for holding " holding-id) " (no holding yet — new-holding intake)")))

(defn- infer [_store {:keys [op stake farmer-id holding-id] :as request}]
  (let [base {:op op
              :effect :propose
              :farmer-id farmer-id
              :holding-id holding-id
              :stake (or stake :low)
              :confidence (case (or stake :low) :high 0.7 :medium 0.85 :low 0.95)
              :rationale (rationale-for op holding-id)}]
    (merge base
           (case op
             :log-work-record
             (select-keys request [:work-id :work-type :detail :timestamp])
             :schedule-farm-operation
             (select-keys request [:task-id :proposed-time :assignee])
             :flag-livelihood-concern
             (select-keys request [:concern-type :note])
             :coordinate-supply-order
             (select-keys request [:item :cost :vendor])
             {}))))

(defn mock-advisor []
  (reify Advisor
    (-advise [_ store request] (infer store request))))

(def ^:private system-prompt
  "You are a farm record-keeping/logistics coordination advisor for a
   subsistence-mixed-crop-and-livestock-farming household. Given a
   request, propose an :op, the :farmer-id and (when relevant)
   :holding-id plus the op's own fields, an honest :confidence and a
   :stake. You are a farm record-keeping/logistics coordination robot
   ONLY — you help log planting/harvest/feeding-schedule/animal-
   condition-check-in records, schedule household labor/task
   operations, and coordinate seeds/feed/tools procurement orders.
   Never propose an op outside the closed four-op allowlist
   (:log-work-record, :schedule-farm-operation,
   :flag-livelihood-concern, :coordinate-supply-order), and NEVER
   propose finalizing a planting/harvest-timing agronomic decision, an
   animal-treatment/welfare/breeding decision, or overriding the
   farmer's own judgment about their household's crops or livestock —
   that authority does not exist for you, under any circumstance, at
   any confidence level, in any phase. A :log-work-record entry is
   planting/harvest/feeding-schedule/animal-condition-check-in metadata
   only, never an agronomic decision, a treatment decision or a
   breeding decision. A :schedule-farm-operation proposal is household
   labor/task scheduling logistics only — never a planting/harvest
   directive, a treatment directive, or a farmer-judgment override. You
   never perform farm labor, never handle the animals, and never
   directly make an agronomic, treatment, welfare or breeding decision.
   Any indication that a crop-failure, animal-welfare, injury-risk, or
   food-security concern needs human attention must be surfaced only
   via :flag-livelihood-concern, which always requires human review
   regardless of confidence. The governor independently verifies
   farmer/holding registration and always escalates livelihood-concern
   flags and above-threshold supply orders to a human.")

(defn- parse-proposal [content]
  (try
    (let [p (edn/read-string content)]
      (if (map? p)
        (assoc p :effect :propose)
        {:op :unknown :effect :propose :confidence 0.0 :stake :high
         :rationale "unparseable LLM response"}))
    (catch #?(:clj Exception :cljs js/Error) _
      {:op :unknown :effect :propose :confidence 0.0 :stake :high
       :rationale "LLM response parse failure"})))

(defn llm-advisor
  [chat-model model-generate-fn gen-opts]
  (reify Advisor
    (-advise [_ _store request]
      (let [msgs [{:role :system :content system-prompt}
                  {:role :user :content (str "operation request: " (pr-str request))}]
            resp (model-generate-fn chat-model msgs gen-opts)]
        (parse-proposal (:content resp))))))
