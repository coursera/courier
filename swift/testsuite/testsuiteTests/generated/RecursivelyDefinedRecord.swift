import Foundation
import SwiftyJSON

public struct RecursivelyDefinedRecord: JSONSerializable, DataTreeSerializable {
    
    public let `self`: RecursivelyDefinedRecord?
    
    public init(
        `self`: RecursivelyDefinedRecord?
    ) {
        self.`self` = `self`
    }
    
    public static func readJSON(json: JSON) throws -> RecursivelyDefinedRecord {
        return RecursivelyDefinedRecord(
            `self`: try json["self"].json.map { try RecursivelyDefinedRecord.readJSON($0) }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> RecursivelyDefinedRecord {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let `self` = self.`self` {
            dict["self"] = `self`.writeData()
        }
        return dict
    }
}
