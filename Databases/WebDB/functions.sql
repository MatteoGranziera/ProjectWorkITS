CREATE FUNCTION addscore (varchar,varchar, integer, date) RETURNS boolean AS '
DECLARE
-- Declare aliases for function arguments.
lang ALIAS FOR $1;
count ALIAS FOR $2;
sc ALIAS FOR $3;
mo ALIAS FOR $4;

  id_l INTEGER;
  id_c INTEGER;
  id_s INTEGER;

BEGIN

  SELECT INTO id_l id FROM languages WHERE
    name = lang;

  IF id_l IS NULL THEN
    RETURN FALSE;
  END IF;

  SELECT INTO id_c id FROM countries WHERE id_state_twitter=count;

  IF id_c IS NULL THEN
    RETURN FALSE;
  END IF;

  SELECT INTO id_s id FROM scores WHERE id_language = id_l AND id_country = id_c;

  IF id_s IS NULL THEN
    INSERT INTO scores(id_country, id_language, score, month) VALUES ( id_c, id_l, sc, mo );
    SELECT INTO id_s id FROM scores WHERE id_language = id_l AND id_country = id_c;
  END IF;

  UPDATE scores SET score = score + sc WHERE id = id_s;
  RETURN TRUE;

END;
' LANGUAGE 'plpgsql';