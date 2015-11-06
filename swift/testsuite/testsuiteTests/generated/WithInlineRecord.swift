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
    
    public static func readJSON(json: JSON) throws -> WithInlineRecord {
        return WithInlineRecord(
            inline: try json["inline"].json.map { try InlineRecord.readJSON($0) },
            inlineOptional: try json["inlineOptional"].json.map { try InlineOptionalRecord.readJSON($0) }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> WithInlineRecord {
        return try readJSON(JSON(data))
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
