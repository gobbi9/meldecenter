package coding.challenge.meldecenter.ausgehend.export

import kotlinx.coroutines.flow.Flow

interface Exporter {
    fun export(): Flow<ExportEntity>
}
