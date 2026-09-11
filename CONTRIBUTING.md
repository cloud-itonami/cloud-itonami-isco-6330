# Contributing

`cloud-itonami-isco-6330` accepts contributions to the OSS actor, policy tests,
documentation, examples and open occupation blueprint.

## Development

```bash
kbb -M:test
kbb -M:lint
```

Keep changes small and include tests for policy, audit, store or disclosure
behavior.

## Rules

- Do not commit real farmer, plot, holding, animal, work or operator data.
- Keep production writes and disclosures behind MixedFarmGovernor.
- Never add an op, anywhere in the closed allowlist, that finalizes a
  planting/harvest-timing agronomic decision or an animal-treatment/
  welfare/breeding decision, or overrides the farmer's own judgment
  about their household's crops or livestock — this boundary is
  permanent, not a default to be relaxed later.
- Treat this occupation's workflows as high-risk: add tests for
  permission, purpose, scope-exclusion, animal-welfare,
  household-livelihood and audit logging.
- Document any new business-model or operator assumption in `docs/`.

## Pull Requests

PRs should describe:

- what behavior changed
- which policy invariant is affected
- how it was tested
- whether operator or certification docs need updates
