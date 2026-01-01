package coding.challenge.meldecenter.ausgehend.sozialversicherung.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.Exporter
import io.micrometer.tracing.annotation.NewSpan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.springframework.stereotype.Service

/**
 * Implementierung des [Exporter]-Interfaces für DEÜV-Anmeldungen.
 *
 * Dieser Service koordiniert den gesamten Exportprozess: Gruppierung, Zuweisung und Upload.
 *
 * @property repository Das Repository für DEÜV-Anmeldungen.
 * @property exportCreator Der Service zur Erstellung von Exporten.
 * @property exportUploader Der Service zum Hochladen der Exportdateien.
 */
@Service
class DeuevAnmeldungExporter(
    private val repository: DeuevAnmeldungExportRepository,
    private val exportCreator: DeuevAnmeldungExportCreator,
    private val exportUploader: DeuevAnmeldungExportUploader,
) : Exporter {
    /**
     * Führt den Exportvorgang für alle verfügbaren DEÜV-Anmeldungs-Gruppen aus.
     *
     * @return Ein [Flow] der erfolgreich verarbeiteten [ExportEntity]-Objekte.
     */
    @NewSpan
    override fun export(): Flow<ExportEntity> =
        repository
            .findAllExportGroups()
            .mapNotNull { exportCreator.assign(exportGroup = it) }
            .mapNotNull { exportUploader.upload(unpreparedExport = it) }
}
