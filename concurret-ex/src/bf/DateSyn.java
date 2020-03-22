package bf;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateSyn {
    private static Map<String, Date> lastLogin = Collections.synchronizedMap(new HashMap<>());
}
