-- Poseu el codi dels procediments/funcions emmagatzemats, triggers, ..., usats al projecte

CREATE OR REPLACE TRIGGER usuari_tri 
BEFORE INSERT ON USUARIS 
FOR EACH ROW 
BEGIN 
    SELECT NVL(MAX(ID), 0) + 1 INTO :NEW.ID FROM USUARIS; 
END;
/

