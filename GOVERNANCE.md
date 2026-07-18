# Governance

`cloud-itonami-isco-6330` is an OSS open-occupation blueprint. Governance covers
both code and the operator model.

## Maintainers

Maintainers may merge changes that preserve these invariants:

- the Advisor cannot directly dispatch robot actions, perform farm
  labor, handle the animals, or disclose records.
- MixedFarmGovernor remains independent of the advisor.
- hard policy violations cannot be overridden by human approval.
- the closed op allowlist never gains an op that finalizes a
  planting/harvest-timing agronomic decision or an animal-treatment/
  welfare/breeding decision, or overrides the farmer's own judgment
  about their household's crops or livestock.
- every commit, hold and approval path is auditable.
- real farmer/plot/holding/animal/work/operator data stays outside Git.

## Decision Records

Architecture decisions live in `docs/adr/`. Changes to the trust model,
storage contract, public business model, operator certification, license, or
the closed op-allowlist should add or update an ADR.

## Operator Governance

Anyone may fork and operate independently. itonami.cloud certification is a
separate trust mark and should require security, audit, support and data-flow
review.

Certified operators can lose certification for:

- bypassing policy checks
- mishandling farmer/plot/holding/animal/work/operator data
- misrepresenting certification status
- failing to respond to security incidents
- hiding material changes to customer-facing operation
- attempting to route a planting/harvest-timing agronomic decision, an
  animal-treatment/welfare/breeding decision, or a farmer-judgment
  override, through this actor by any means
