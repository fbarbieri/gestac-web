package ort.proyecto.gestac.core.entities.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.AreaBestSource;
import ort.proyecto.gestac.core.entities.Source;

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

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void updateBestSourceForArea() {
		List<Object[]> list = em.createQuery(""
				+ "from Source as source, AreaBestSource as best "
				+ "where source.area.id=best.area.id and "
				+ "source.scoreTotal > best.source.scoreTotal")
				.getResultList();
		
		for (Object[] row : list) {
			Source better = (Source) row [0];
			AreaBestSource best = (AreaBestSource) row[1];
			best.setSource(better);
			em.merge(best);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void addAsBestIfNull(Source source) {
		List<AreaBestSource> bestList = em.createQuery(""
				+ "from AreaBestSource best "
				+ "where best.area.id=?1 "
				+ "and best.source is null").
				setParameter(1, source.getArea().getId()).
				getResultList();
		if (bestList!=null && bestList.size()>0) {
			AreaBestSource best = bestList.get(0);
			best.setSource(source);
			em.merge(best);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isDeletable(Long idToDelete) {
		List<String> knowledges = em.createQuery(""
				+ "select k.id from Knowledge k "
				+ "where k.source.id=?1").
				setParameter(1, idToDelete).
				getResultList();
		
		if (knowledges.size()>0) {
			return false;
		} else {
			List<String> sources = em.createQuery(""
					+ "select best.id from AreaBestSource best "
					+ "where best.source.id=?1").
					setParameter(1, idToDelete).
					getResultList();
			return sources.size()<=0;
		}
		
	}
	
}
