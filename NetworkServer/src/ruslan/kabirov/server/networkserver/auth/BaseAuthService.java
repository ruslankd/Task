package ruslan.kabirov.server.networkserver.auth;

import java.util.Map;
import java.util.Objects;

public class BaseAuthService implements AuthService {

    private static class AuthEntry {
        private String login;
        private String password;

        public AuthEntry(String login, String password) {
            this.login = login;
            this.password = password;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AuthEntry authEntry = (AuthEntry) o;
            return Objects.equals(login, authEntry.login) &&
                    Objects.equals(password, authEntry.password);
        }

        @Override
        public int hashCode() {
            return Objects.hash(login, password);
        }
    }


    private static final Map<AuthEntry, String> NICKNAME_BY_LOGIN_AND_PASS = Map.of(
            new AuthEntry("login1", "pass1"), "nickname1",
            new AuthEntry("login2", "pass2"), "nickname2",
            new AuthEntry("login3", "pass3"), "nickname3"
    );


    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        return NICKNAME_BY_LOGIN_AND_PASS.get(new AuthEntry(login, password));
    }

    @Override
    public void start() {
        System.out.println("Auth service has been started");
    }

    @Override
    public void stop() {
        System.out.println("Auth service has been stopped");
    }
}
