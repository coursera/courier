import Foundation
import SwiftyJSON

struct RecursivelyDefinedRecord: JSONSerializable {
    
    let `self`: RecursivelyDefinedRecord?
    
    init(
        `self`: RecursivelyDefinedRecord?
    ) {
        self.`self` = `self`
    }
    
    static func read(json: JSON) -> RecursivelyDefinedRecord {
        return RecursivelyDefinedRecord(
            `self`: json["self"].json.map { RecursivelyDefinedRecord.read($0) }
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let `self` = self.`self` {
            json["self"] = `self`.write()
        }
        return JSON(json)
    }
}
