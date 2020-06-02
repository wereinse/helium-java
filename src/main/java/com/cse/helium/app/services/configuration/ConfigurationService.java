package com.cse.helium.app.services.configuration;

import com.cse.helium.app.services.keyvault.IEnvironmentReader;
import com.cse.helium.app.services.keyvault.IKeyVaultService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService implements IConfigurationService {
  private static final Logger logger = LogManager.getLogger(ConfigurationService.class);

  int delay = 1000;
  int period = 1000;

  private IKeyVaultService keyVaultService;
  private IEnvironmentReader environmentReader;

  Map<String, String> configEntries = new ConcurrentHashMap<String, String>();

  public Map<String, String> getConfigEntries() {
    return configEntries;
  }

  /** ConfigurationService. */
  @SuppressFBWarnings("DM_EXIT")
  @Autowired
  public ConfigurationService(IKeyVaultService kvService, IEnvironmentReader envReader)
      throws InterruptedException {

    int maxRetries = 10;
    int retries = 0;

    while (retries <= maxRetries) {

        if (kvService == null) {
          logger.info("keyVaultService is null");
          System.exit(-1);
        }

        keyVaultService = kvService;
        environmentReader = envReader;

        String authType = environmentReader.getAuthType();
        System.out.println("authType is " + authType);

        retries++;
        System.out.println("number of retries " + retries);
        if (authType.contains("CLI") && retries <= maxRetries) {
          System.out.println("Key Vault: Retry");
          // wait 1 second and retry (continue while loop)

          System.out.println("checkstart " + DateTime.now().getMillis());
          // wait(1000);
          Thread.sleep(1000);
          System.out.println("checkend " + DateTime.now().getMillis());
        }

        Map<String, String> secrets = keyVaultService.getSecrets();
        logger.info("Secrets are " + (secrets == null ? "NULL" : "NOT NULL"));
        configEntries = secrets;
        //return;


    }
  }
}
