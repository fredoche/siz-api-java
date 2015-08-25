package io.siz.repository.siz;

import com.mongodb.WriteResult;
import io.siz.domain.siz.Event;

/**
 *
 * @author fred
 */
public interface ViewerProfileRepositoryCustom {

    WriteResult updateFromEvent(Event e);

}
