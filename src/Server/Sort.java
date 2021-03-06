package Server;

import Logic.Lists.SendList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.io.IOException;

@Path("sort")
public class Sort {

    final Logger logger = LoggerFactory.getLogger(Sort.class);

    @GET
    @Produces("application/xml")
    /**
     * Applies a Sorting method to the list receive
     * @param method Sort algorithm
     * @param list list
     * @return XML of the sorted List
     */
    public String sort(@QueryParam("method") String method, @QueryParam("list") String list) throws IOException {
        logger.info("Sorting the list.");
        SendList sendList = Serializer.deserializadorString(list);
        switch (method){
            case "selection":
                sendList.selectionSort();
                break;
            case "insertion":
                sendList.insertionSort();
                break;
            case "quicksort":
                sendList.quickSort();
                break;
            case "btree":
                sendList = sendList.binaryGUIHelper();
                break;
            case "avl":
                sendList = sendList.AVLGUIHelper();
                break;
        }

        return Serializer.serializadorString(sendList);
    }
}
