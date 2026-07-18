# Security Policy

This project handles subsistence-mixed-crop-and-livestock-farmers farm
record-keeping and logistics operating workflows. Treat vulnerabilities as
potentially high impact even when the demo data is synthetic — this domain
has a livelihood-vulnerability dimension, an animal-welfare dimension,
agronomic-decision authority, and standard agricultural/animal-handling
physical-safety hazards.

## Do Not Disclose Publicly

Report privately before opening public issues for:

- credential exposure
- real farmer, plot, holding, animal, work or operator data exposure
- authorization bypass
- MixedFarmGovernor bypass
- any path that lets a proposal finalize a planting/harvest-timing
  agronomic decision or an animal-treatment/welfare/breeding decision,
  or override the farmer's own judgment about their household's crops
  or livestock
- audit-ledger tampering
- over-disclosure in reports or exports
- unsafe robot action dispatch

## Reporting

Use GitHub private vulnerability reporting when available for the repository.
If that is unavailable, contact the repository maintainers through the
cloud-itonami organization before publishing details.

Include:

- affected commit or version
- reproduction steps
- expected and actual behavior
- impact on farmer/plot/holding/animal/work data, policy enforcement or
  audit logging
- suggested fix, if known

## Production Guidance

- Store secrets outside Git.
- Keep real farmer/plot/holding/animal/work/operator data outside this
  repository.
- Run policy tests before deployment.
- Export and review audit logs regularly.
- Use least privilege for operators and service accounts.
