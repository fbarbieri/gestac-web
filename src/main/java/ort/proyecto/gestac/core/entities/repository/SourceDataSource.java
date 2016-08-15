package ort.proyecto.gestac.core.entities.repository;

import ort.proyecto.gestac.core.entities.Area;

public interface SourceDataSource {
	
	public Area getAreaForBestSource(Long sourceId);

}
