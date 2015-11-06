import Foundation
import SwiftyJSON

public struct WithRecordArray: JSONSerializable, DataTreeSerializable, Equatable {
    
    public let empties: [Empty]?
    
    public let fruits: [Fruits]?
    
    public init(
        empties: [Empty]?,
        fruits: [Fruits]?
    ) {
        self.empties = empties
        self.fruits = fruits
    }
    
    public static func readJSON(json: JSON) throws -> WithRecordArray {
        return WithRecordArray(
            empties: try json["empties"].array.map { try $0.map { try Empty.readJSON($0.jsonValue) } },
            fruits: json["fruits"].array.map { $0.map { Fruits.read($0.stringValue) } }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> WithRecordArray {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let empties = self.empties {
            dict["empties"] = empties.map { $0.writeData() }
        }
        if let fruits = self.fruits {
            dict["fruits"] = fruits.map { $0.write() }
        }
        return dict
    }
}
public func ==(lhs: WithRecordArray, rhs: WithRecordArray) -> Bool {
    return (
        (lhs.empties == nil ? (rhs.empties == nil) : lhs.empties! == rhs.empties!) &&
        (lhs.fruits == nil ? (rhs.fruits == nil) : lhs.fruits! == rhs.fruits!) &&
        true
    )
}
