package com.michelin.cio.hudson.plugins.rolestrategy;

import com.google.common.annotations.VisibleForTesting;
import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import jenkins.model.GlobalConfigurationCategory;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Configuration of security options for {@link RoleBasedAuthorizationStrategy}.
 *
 * @author Thomas Nemer
 * @since 3.2
 */
@Extension
public class RoleStrategySecurityConfig extends GlobalConfiguration {

    private static final RoleStrategySecurityConfig DEFAULT =
            new RoleStrategySecurityConfig(false);

    /**
     * If enabled, using a PermissionHelper to get a set of Permissions containing
     * dangerous permissions will log a warning instead of throwing a SecurityException.
     * In any case, the permission is dismissed from the set.
     */
    private boolean permissive;

    @DataBoundConstructor
    public RoleStrategySecurityConfig() {
        load();
    }

    /**
     * Constructor.
     *
     * @param permissive allows to log dangerous permissions instead of throwing exceptions.
     */
    RoleStrategySecurityConfig(boolean permissive) {
        this.permissive = permissive;
    }

    /**
     * Gets the default configuration of {@link RoleBasedAuthorizationStrategy}
     *
     * @return Default configuration
     */
    @Nonnull
    public static final RoleStrategySecurityConfig getDefault() {
        return DEFAULT;
    }

    /**
     * Configuration method for testing purposes.
     *
     * @param permissive allows to log dangerous permissions instead of throwing exceptions.
     * @return true if the configuration successful
     * @throws IllegalStateException Cannot retrieve the plugin config instance
     */
    @VisibleForTesting
    static boolean configure(boolean permissive) {
        RoleStrategySecurityConfig instance = getInstance();
        if (null != instance) {
            instance.permissive = permissive;
            instance.save();
            return true;
        } else {
            throw new IllegalStateException("Cannot retrieve the plugin config instance");
        }
    }

    @CheckForNull
    public static RoleStrategySecurityConfig getInstance() {
        return RoleStrategySecurityConfig.all().get(RoleStrategySecurityConfig.class);
    }

    /**
     * Retrieves the Role Based Strategy security configuration.
     *
     * @return Settings
     * @throws IllegalStateException The configuration cannot be retrieved
     */
    @Nonnull
    public static RoleStrategySecurityConfig getOrFail() throws IllegalStateException {
        RoleStrategySecurityConfig c = RoleStrategySecurityConfig.all().get(RoleStrategySecurityConfig.class);
        if (null != c) {
            return c;
        } else {
            throw new IllegalStateException("Cannot retrieve the Role Based Strategy plugin configuration");
        }
    }

    public boolean isPermissive() {
        return permissive;
    }

    @DataBoundSetter
    public void setPermissive(boolean permissive) {
        this.permissive = permissive;
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        final boolean newPermissive = json.getBoolean("permissive");
        return configure(newPermissive);
    }

    @Override
    public GlobalConfigurationCategory getCategory() {
        return GlobalConfigurationCategory.get(GlobalConfigurationCategory.Security.class);
    }
}

