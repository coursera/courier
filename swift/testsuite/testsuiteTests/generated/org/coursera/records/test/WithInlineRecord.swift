import Foundation
import SwiftyJSON

struct WithInlineRecord: JSONSerializable {
    
    let inline: InlineRecord?
    
    let inlineOptional: InlineOptionalRecord?
    
    init(
        inline: InlineRecord?,
        inlineOptional: InlineOptionalRecord?
    ) {
        self.inline = inline
        self.inlineOptional = inlineOptional
    }
    
    static func read(json: JSON) -> WithInlineRecord {
        return WithInlineRecord(
            inline: json["inline"].json.map { InlineRecord.read($0) },
            inlineOptional: json["inlineOptional"].json.map { InlineOptionalRecord.read($0) }
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let inline = self.inline {
            json["inline"] = inline.write()
        }
        if let inlineOptional = self.inlineOptional {
            json["inlineOptional"] = inlineOptional.write()
        }
        return JSON(json)
    }
}
