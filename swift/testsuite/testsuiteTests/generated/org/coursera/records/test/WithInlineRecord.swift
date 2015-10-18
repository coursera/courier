import Foundation
import SwiftyJSON

struct WithInlineRecord {
    
    let inline: InlineRecord?
    
    let inlineOptional: InlineOptionalRecord?
    
    init(inline: InlineRecord?, inlineOptional: InlineOptionalRecord?) {
        
        self.inline = inline
        
        self.inlineOptional = inlineOptional
    }
    
    static func read(json: JSON) -> WithInlineRecord {
        return WithInlineRecord(
        inline: json["inline"].json.map { InlineRecord.read($0) },
        inlineOptional: json["inlineOptional"].json.map { InlineOptionalRecord.read($0) })
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        if let inline = self.inline {
            json["inline"] = JSON(inline.write())
        }
        if let inlineOptional = self.inlineOptional {
            json["inlineOptional"] = JSON(inlineOptional.write())
        }
        
        return json
    }
}
