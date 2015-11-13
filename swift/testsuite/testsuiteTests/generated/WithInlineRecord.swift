import Foundation
import SwiftyJSON

public struct WithInlineRecord: Serializable {
    
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
            inline: try json["inline"].optional(.Dictionary).json.map { try InlineRecord.readJSON($0) },
            inlineOptional: try json["inlineOptional"].optional(.Dictionary).json.map { try InlineOptionalRecord.readJSON($0) }
        )
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
