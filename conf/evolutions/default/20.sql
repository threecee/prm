# --- !Ups

CREATE INDEX components_componenttype_index ON components(componenttype);
CREATE INDEX components_equipmentstate_index ON components(equipmentstate);
CREATE INDEX components_powerunit_index ON components(powerunit);
CREATE INDEX components_powerstation_index ON components(powerstation);

CREATE INDEX componenttypes_name_index ON componenttypes(name);

CREATE INDEX downtimecosts_powerunit_index ON downtimecosts(powerunit);
CREATE INDEX downtimecosts_powerstation_index ON downtimecosts(powerstation);

CREATE INDEX groups_name_index ON groups(name);

CREATE INDEX regions_name_index ON regions(name);

CREATE INDEX incidenttypes_name_index ON incidenttypes(name);

CREATE INDEX powerstations_name_index ON powerstations(name);
CREATE INDEX powerstations_group_index ON powerstations(group_id);
CREATE INDEX powerstations_region_index ON powerstations(region);
CREATE INDEX powerstations_reference_index ON powerstations(referenceid);

CREATE INDEX powerunits_powerstation_index ON powerunits(powerstation);
CREATE INDEX powerunits_reference_index ON powerunits(referenceid);

CREATE INDEX repairs_componenttype_index ON repairs(componenttype);
CREATE INDEX repairs_component_index ON repairs(component);

CREATE INDEX residuallifespans_componenttype_index ON residuallifespans(componenttype);
CREATE INDEX residuallifespans_equipmentstate_index ON residuallifespans(equipmentstate);


# --- !Downs

DROP INDEX IF EXISTS components_componenttype_index;
DROP INDEX IF EXISTS components_equipmentstate_index;
DROP INDEX IF EXISTS components_powerunit_index;
DROP INDEX IF EXISTS components_powerstation_index;

DROP INDEX IF EXISTS componenttypes_name_index;

DROP INDEX IF EXISTS downtimecosts_powerunit_index;
DROP INDEX IF EXISTS downtimecosts_powerstation_index;

DROP INDEX IF EXISTS groups_name_index;

DROP INDEX IF EXISTS regions_name_index;

DROP INDEX IF EXISTS incidenttypes_name_index;

DROP INDEX IF EXISTS powerstations_name_index;
DROP INDEX IF EXISTS powerstations_group_index ;
DROP INDEX IF EXISTS powerstations_region_index;
DROP INDEX IF EXISTS powerstations_reference_index;

DROP INDEX IF EXISTS powerunits_powerstation_index;
DROP INDEX IF EXISTS powerunits_reference_index;

DROP INDEX IF EXISTS repairs_componenttype_index;
DROP INDEX IF EXISTS repairs_component_index ;

DROP INDEX IF EXISTS residuallifespans_componenttype_index;
DROP INDEX IF EXISTS residuallifespans_equipmentstate_index;


