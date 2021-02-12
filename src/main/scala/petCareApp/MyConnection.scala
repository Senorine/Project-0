package petCareApp

import java.sql.DriverManager
import java.sql.Connection

object MyConnection
{
    var myConn: Connection = null;

    def getConnection(): Connection =
    {
              

        if(myConn == null || myConn.isClosed())
        {
            classOf[org.postgresql.Driver].newInstance()
            myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "", "")
        }
        else
        {
            myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "", "")
        }

        return myConn

    }
}
