# Operator Guide

## First Deployment

1. Define the holding's service area and farmer-intake process.
2. Register and verify each farmer/holder and holding before enabling
   any proposal for them.
3. Run synthetic operating cases (work logging across both planting/
   harvest and feeding/condition-checkin work types, farm-operation
   scheduling, supply coordination, livelihood-concern flagging).
4. Enable human-reviewed sign-off for `:high`/`:livelihood-critical`
   actions — this includes every `:flag-livelihood-concern` and every
   above-threshold `:coordinate-supply-order`, with no exception.
5. Measure operating outcomes and audit coverage.

## Minimum Production Controls

- farmer/holder and holding provenance log (registered AND verified
  before any action)
- livelihood-critical escalation path for crop-failure/welfare/
  injury-risk/food-security concerns
- provenance for all operating records
- human review for high-risk cases
- audit export for all gated actions

## Certification

Certified operators must prove that MixedFarmGovernor gates every
livelihood-critical robot action, that livelihood-critical risks
escalate to humans, and that no configuration or fork can introduce an
op that finalizes a planting/harvest-timing agronomic decision or an
animal-treatment/welfare/breeding decision, or overrides the farmer's
own judgment about their household's crops or livestock.
