package uk.ac.ebi.restclient.stubs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by chojnasm on 16/08/2016.
 *
 * Used to parse XML returned by endpoint /parameters/
 */

@XmlRootElement(name="parameters")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsParameters {

    @XmlElement(name="id")
    protected List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
