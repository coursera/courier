import { CustomInt } from "./org.coursera.customtypes.CustomInt";

export interface WithOmitField {
  
  keep : number;
  
  omit : number;
  
  omitCustom : CustomInt;
  
  keepCustom : CustomInt;
}
