import Foundation
import SwiftyJSON

public struct RecursivelyDefinedRecord: JSONSerializable, DataTreeSerializable {
    
    public let `self`: RecursivelyDefinedRecord?
    
    public init(
        `self`: RecursivelyDefinedRecord?
    ) {
        self.`self` = `self`
    }
    
    public static func readJSON(json: JSON) -> RecursivelyDefinedRecord {
        return RecursivelyDefinedRecord(
            `self`: json["self"].json.map { RecursivelyDefinedRecord.readJSON($0) }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> RecursivelyDefinedRecord {
        return readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let `self` = self.`self` {
            dict["self"] = `self`.writeData()
        }
        return dict
    }
}
