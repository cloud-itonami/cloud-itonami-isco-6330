# cloud-itonami-isco-6330

Open Occupation Blueprint for **ISCO-08 6330**: Subsistence Mixed Crop
and Livestock Farmers.

This repository designs a forkable OSS business for a subsistence-
mixed-crop-and-livestock-farm record-keeping and logistics
coordination practice: a farm-logistics robot manages planting/
harvest/feeding-schedule/condition-check-in records, household labor/
task scheduling, and seeds/feed/tools procurement coordination under a
governor-gated actor — and structurally **never** finalizes a
planting/harvest-timing agronomic decision or an animal-treatment/
welfare/breeding decision, and **never** overrides the farmer's own
judgment about their household's crops or livestock.

## This actor has no agronomic or animal-treatment/welfare/breeding or farmer-override authority

Subsistence Mixed Crop and Livestock Farmers grow crops AND raise
animals for household consumption — this combines the **livelihood-
vulnerability dimension** (crop/livestock loss directly threatens
household food security) with an **animal-welfare dimension** AND
**agronomic-decision authority**. **This actor is a farm record-
keeping/logistics coordination robot ONLY.** It never performs farm
labor, never handles the animals, and never directly makes an
agronomic (planting/harvest-timing), treatment, welfare or breeding
decision. It has NO op, anywhere in its allowlist, that resembles
finalizing a planting/harvest-timing agronomic decision, an animal-
treatment/welfare/breeding decision, or overriding the farmer's own
judgment about their household's crops or livestock. These are
**structurally absent from the closed op-allowlist entirely**, not
merely gated behind escalation — under any circumstance, at any
confidence level, in any phase. Any observation the robot logs that
suggests a crop-failure, animal-welfare, injury-risk, or food-security
concern is surfaced ONLY via an always-escalating
`:flag-livelihood-concern` op that a human reviews and acts on
entirely themselves. This mirrors the Wave4 person-facing-service
safety guardrail (ADR-2607152500): decisions directly touching a
planting/harvest-timing agronomic choice, an animal's treatment/
welfare/breeding, or a household's own crop/livestock judgment, always
exclude the closed op allowlist and always escalate. This actor's role
ends at "here is the planting/harvest/feeding/roster/condition-
check-in status" — it has zero authority over agronomic, treatment,
welfare, or breeding decisions, which remain entirely with the human
farmer, at all times, with zero exception.

**Maturity: `:implemented`.** `src/mixedfarm/` implements the
`MixedFarmActor` as a `langgraph.graph/state-graph` (`mixedfarm.actor`)
wired to a `Farm Operations Advisor` (`mixedfarm.advisor`) and an
independent `MixedFarmGovernor` (`mixedfarm.governor`), following the
itonami actor pattern (ADR-2607121000): `:intake -> :advise -> :govern
-> :decide -+-> :commit (:ok?) +-> :request-approval (:escalate?,
human-in-the-loop interrupt) +-> :hold (:hard?)`. Run `clojure -M:test`
for the current test count.

HARD invariants (always hold, never overridable): farmer provenance (a
proposal must resolve to an independently registered AND verified
farmer/holding record), a closed four-op proposal allowlist (any op
outside it — including anything that would finalize a planting/
harvest-timing agronomic decision, an animal-treatment/welfare/
breeding decision, or override the farmer's own judgment about their
household's crops or livestock — is a permanent HARD block, because no
such op exists in the allowlist to begin with), no-actuation
(`:effect` must be `:propose`), a registered-and-verified holding
basis (for the three ops that reference one), a work-record-decision-
forbidden check (`:log-work-record` may only carry planting/harvest/
feeding-schedule/animal-condition-check-in metadata, never an
agronomic, treatment, welfare, or breeding decision), a farm-schedule-
override-forbidden check (`:schedule-farm-operation` may only carry
household labor/task scheduling logistics, never a planting/harvest
directive, a treatment directive, or a farmer-judgment override), and
a content-based scope-exclusion check: any proposal whose free text
names a finalization/execution action for a planting/harvest-timing
agronomic decision, an animal-treatment/welfare/breeding decision, or
an override of the farmer's own judgment about their household's crops
or livestock, is a permanent HARD block, independent of and in
addition to the op-allowlist check. This actor **never** exercises,
simulates exercising, or proposes exercising any agronomic, animal-
treatment/welfare/breeding decision, or any override of the farmer's
own judgment about their household's crops or livestock — it only
documents farm records and coordinates logistics.

Always-escalate (human sign-off regardless of confidence, mapping this
repo's Trust Controls in
[`docs/business-model.md`](docs/business-model.md)):
`:flag-livelihood-concern` (surfacing a crop-failure, animal-welfare,
injury-risk, or food-security concern that needs human review —
always requires human review; never auto-resolved, never in any
phase's auto-commit set — this is the ONLY channel by which such a
concern may be surfaced) and any `:coordinate-supply-order` above the
registered per-holding cost threshold.

## Robotics premise

All cloud-itonami verticals are designed on the premise that a **robot
performs the physical/administrative domain work**. Here a farm-
logistics robot performs planting/harvest/feeding-schedule/animal-
condition-check-in data entry, household labor/task scheduling, and
seeds/feed/tools procurement coordination under an actor that proposes
actions and an independent **MixedFarmGovernor** that gates them. The
governor never dispatches hardware itself; `:high`/`:livelihood-
critical` actions (such as flagging a livelihood concern, or an
above-threshold supply order) require human sign-off — and no action
in this actor's closed op allowlist can ever finalize a planting/
harvest-timing agronomic decision, an animal-treatment/welfare/
breeding decision, or override the farmer's own judgment about their
household's crops or livestock. This actor coordinates FARM RECORD-
KEEPING/LOGISTICS ONLY — it never performs farm labor, never handles
the animals, and never makes agronomic or treatment decisions itself.

## Core Contract

```text
farmer intake queue + holding roster directory + supply policy
        |
        v
Farm Operations Advisor -> MixedFarmGovernor -> log record/coordinate, or human sign-off
        |
        v
robot actions (gated) + operating records + audit ledger
```

No automated advice can dispatch a robot action the governor refuses,
finalize a planting/harvest-timing agronomic decision or an animal-
treatment/welfare/breeding decision, override the farmer's own
judgment about their household's crops or livestock, suppress an
operating record, or disclose sensitive data without governor approval
and audit evidence.

## Capability layer

Resolves via [`kotoba-lang/occupation`](https://github.com/kotoba-lang/occupation)
(ISCO-08 `6330`). Required capabilities:

- :robotics
- :identity
- :audit-ledger

See [`docs/business-model.md`](docs/business-model.md) and
[`docs/operator-guide.md`](docs/operator-guide.md).

## License

AGPL-3.0-or-later.
