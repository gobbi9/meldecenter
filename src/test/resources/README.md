# Mapping JSON -> XML

This table describes the mapping between the incoming JSON files and their corresponding XML representations in the FTP folders.

| JSON Source(s) | FTP Inbox XML | Response XML | Remarks |
| :--- | :--- | :--- | :--- |
| `DEÜV-Anmeldung.json`,<br>`DEÜV-Anmeldung-2.json` | `inbox/DEUEV_ANM_12345678_20250201_0001.xml` | `outbox/DEUEV_RUECK_12345678_20250201_0001.xml` | Mixed results: one ANGENOMMEN and one ABGELEHNT |
| `DEÜV-Anmeldung-3.json` | `inbox/DEUEV_ANM_11223344_20250620_0001.xml` | `error/DEUEV_ERR_11223344_20250620_0001.xml` | Technical error |
| `Entgeltbescheinigung-Arbeitsunfähigkeit.json`,<br>`Entgeltbescheinigung-Arbeitsunfähigkeit-2.json`,<br>`Entgeltbescheinigung-Arbeitsunfähigkeit-3.json` | `inbox/KG_ENTG_12345678_20250310_01.xml` | `outbox/KG_RUECK_12345678_20250310_01.xml` | All ANGENOMMEN |
