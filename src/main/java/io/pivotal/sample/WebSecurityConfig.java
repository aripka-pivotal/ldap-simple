package io.pivotal.sample;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	//local ldif testing versions
	// private String ldapURL = "ldap://localhost:8389";
	// private String rootDn = "dc=springframework,dc=org";
	// private String ldapUserSearchBase = "ou=people";
	// private String ldapGroupSearchBase = "ou=groups";
	private String ldapUserSearchFilter = "uid={0}";
	private String ldapURL = "ldap://ldap.forumsys.com:389";
	private String rootDn = "dc=example,dc=com";
	private String ldapUserSearchBase = "";
	private String ldapGroupSearchBase = "";
	private String ldapGroupFilter = "(uniqueMember={0})";
	private String ldapGroupCN = "SCIENTISTS";

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(ldapAuthenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.authorizeRequests().anyRequest().hasRole(ldapGroupCN).and().formLogin().and().httpBasic();

	}

	@Bean
	public AuthenticationProvider ldapAuthenticationProvider() throws Exception {
		DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(
				Arrays.asList(ldapURL), rootDn);
		contextSource.afterPropertiesSet();
		LdapUserSearch ldapUserSearch = new FilterBasedLdapUserSearch(ldapUserSearchBase, ldapUserSearchFilter,
				contextSource);

		BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
		bindAuthenticator.setUserSearch(ldapUserSearch);

		DefaultLdapAuthoritiesPopulator auths = new DefaultLdapAuthoritiesPopulator(contextSource, ldapGroupSearchBase);

		auths.setGroupSearchFilter(ldapGroupFilter);
		auths.setSearchSubtree(true);
		LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(bindAuthenticator,
				auths);
		return ldapAuthenticationProvider;
	}
}