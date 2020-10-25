package springbook.user.sqlservice.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="sqlmapType", propOrder = { "sql" })
public class Sqlmap
{
    @XmlElement(required = true)
    protected List<SqlType> sql;
    public List<SqlType> getSql()
    {
        if(sql == null)
        {
            sql = new ArrayList<SqlType>();
        }
        return this.sql;
    }
}
