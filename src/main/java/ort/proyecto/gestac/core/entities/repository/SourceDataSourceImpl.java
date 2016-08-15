package ort.proyecto.gestac.core.entities.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.AreaBestSource;
import ort.proyecto.gestac.core.entities.IssueBestKnowledge;

public class SourceDataSourceImpl implements SourceDataSource{

	@PersistenceContext
	private EntityManager em;
	
	
	@SuppressWarnings("unchecked")
	public Area getAreaForBestSource(Long sourceId) {
		
		List<AreaBestSource> list = em.createQuery(""
				+ "select best from AreaBestSource as best where source.id=?1").
				setParameter(1, sourceId).
				getResultList();
		
		if (list!=null && list.size()>0) {
			return list.get(0).getArea();
		} else {
			return null;
		}
		
	}
	
}
