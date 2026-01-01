package coding.challenge.meldecenter.ausgehend.export

import kotlinx.coroutines.flow.Flow

/**
 * Schnittstelle für Export-Komponenten.
 *
 * Jede Implementierung ist für einen bestimmten Meldungstyp verantwortlich.
 */
interface Exporter {
    /**
     * Führt den Export aus.
     *
     * @return Ein [Flow] von [ExportEntity]-Objekten, die während des Vorgangs erstellt oder verarbeitet wurden.
     */
    fun export(): Flow<ExportEntity>
}
