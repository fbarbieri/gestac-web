package ort.proyecto.gestac.core.entities.repository;

import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Source;

public interface SourceDataSource {
	
	public Area getAreaForBestSource(Long sourceId);

	public void updateBestSourceForArea();
	
	public void addAsBestIfNull(Source source);

}
