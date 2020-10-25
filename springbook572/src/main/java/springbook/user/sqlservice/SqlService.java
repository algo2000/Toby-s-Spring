package springbook.user.sqlservice;

import springbook.user.sqlservice.jaxb.SqlRetrievalFailureException;

public interface SqlService
{
    String getSql(String key) throws SqlRetrievalFailureException;
}
