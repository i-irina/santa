import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataClientFactory;
import ru.redcom.lib.integration.api.client.dadata.DaDataException;

import java.math.BigDecimal;

public class Standardization {
    private static final String API_KEY = "023f9f64830e3edacad78527ec5a472583b32ecf";
    private static final String SECRET_KEY = "d2fc6690f46879a70291f2e721e9740cd0dde198";
    private final DaDataClient dadata = DaDataClientFactory.getInstance(API_KEY, SECRET_KEY);

      public String cleanName2(String str) {
        if ((str!=null)&& (str!=" ")&& (str!="")&& (!str.isEmpty())){
        String fname =  dadata.cleanName(str).getResult();
             return fname;
        }
        else
        {
            return str;
        }
    }

    public BigDecimal accountBalance() throws DaDataException {
        final BigDecimal balance = dadata.getProfileBalance();
        System.out.println("Balance = " + balance);
        return balance;
    }

}