import { InlineOptionalRecord } from "./org.coursera.records.test.InlineOptionalRecord";
import { InlineRecord } from "./org.coursera.records.test.InlineRecord";

export interface WithInlineRecord {
  
  inline : InlineRecord;
  
  inlineOptional ?: InlineOptionalRecord;
}
