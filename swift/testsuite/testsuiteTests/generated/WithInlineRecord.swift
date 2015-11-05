import Foundation
import SwiftyJSON

public struct WithInlineRecord: JSONSerializable, DataTreeSerializable {
    
    public let inline: InlineRecord?
    
    public let inlineOptional: InlineOptionalRecord?
    
    public init(
        inline: InlineRecord?,
        inlineOptional: InlineOptionalRecord?
    ) {
        self.inline = inline
        self.inlineOptional = inlineOptional
    }
    
    public static func readJSON(json: JSON) -> WithInlineRecord {
        return WithInlineRecord(
            inline: json["inline"].json.map { InlineRecord.readJSON($0) },
            inlineOptional: json["inlineOptional"].json.map { InlineOptionalRecord.readJSON($0) }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> WithInlineRecord {
        return readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let inline = self.inline {
            dict["inline"] = inline.writeData()
        }
        if let inlineOptional = self.inlineOptional {
            dict["inlineOptional"] = inlineOptional.writeData()
        }
        return dict
    }
}
