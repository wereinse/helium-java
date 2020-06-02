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

    environmentReader = envReader;
    String authType = environmentReader.getAuthType();
    System.out.println("authType is " + authType);

    while (retries <= maxRetries) {
      System.out.println("number of retries " + retries);
      try {
        if (kvService == null) {
          logger.info("keyVaultService is null");
          System.exit(-1);
        }

        keyVaultService = kvService;

        Map<String, String> secrets = keyVaultService.getSecrets();
        logger.info("Secrets are " + (secrets == null ? "NULL" : "NOT NULL"));
        configEntries = secrets;
        return;

      } catch (Exception ex) {
        retries++;
        logger.info("number of retries " + retries);

        if (authType.contains("MSI") && retries <= maxRetries) {
          logger.info("Key Vault: Retry");

          System.out.println("checkstart " + DateTime.now().getMillis());
          Thread.sleep(1000);
          System.out.println("checkend " + DateTime.now().getMillis());
        } else {
          throw new Error("Failed to connect to Key Vault with MSI");
        }
      }
    }
  }
}
