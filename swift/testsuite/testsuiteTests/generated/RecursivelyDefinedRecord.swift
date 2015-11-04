import Foundation
import SwiftyJSON

public struct RecursivelyDefinedRecord: JSONSerializable {
    
    public let `self`: RecursivelyDefinedRecord?
    
    public init(
        `self`: RecursivelyDefinedRecord?
    ) {
        self.`self` = `self`
    }
    
    public static func read(json: JSON) -> RecursivelyDefinedRecord {
        return RecursivelyDefinedRecord(
            `self`: json["self"].json.map { RecursivelyDefinedRecord.read($0) }
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let `self` = self.`self` {
            json["self"] = `self`.write()
        }
        return JSON(json)
    }
}
