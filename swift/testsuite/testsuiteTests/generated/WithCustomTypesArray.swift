import Foundation
import SwiftyJSON

public struct WithCustomTypesArray: JSONSerializable, DataTreeSerializable, Equatable {
    
    public let ints: [Int]?
    
    public let arrays: [[Simple]]?
    
    public let maps: [[String: Simple]]?
    
    public let unions: [WithCustomTypesArrayUnion]?
    
    public let fixed: [String]?
    
    public init(
        ints: [Int]?,
        arrays: [[Simple]]?,
        maps: [[String: Simple]]?,
        unions: [WithCustomTypesArrayUnion]?,
        fixed: [String]?
    ) {
        self.ints = ints
        self.arrays = arrays
        self.maps = maps
        self.unions = unions
        self.fixed = fixed
    }
    
    public static func readJSON(json: JSON) -> WithCustomTypesArray {
        return WithCustomTypesArray(
            ints: json["ints"].array.map { $0.map { $0.intValue } },
            arrays: json["arrays"].array.map { $0.map { $0.arrayValue.map { Simple.readJSON($0.jsonValue) } } },
            maps: json["maps"].array.map { $0.map { $0.dictionaryValue.mapValues { Simple.readJSON($0.jsonValue) } } },
            unions: json["unions"].array.map { $0.map { WithCustomTypesArrayUnion.readJSON($0.jsonValue) } },
            fixed: json["fixed"].array.map { $0.map { $0.stringValue } }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> WithCustomTypesArray {
        return readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let ints = self.ints {
            dict["ints"] = ints
        }
        if let arrays = self.arrays {
            dict["arrays"] = arrays.map { $0.map { $0.writeData() } }
        }
        if let maps = self.maps {
            dict["maps"] = maps.map { $0.mapValues { $0.writeData() } }
        }
        if let unions = self.unions {
            dict["unions"] = unions.map { $0.writeData() }
        }
        if let fixed = self.fixed {
            dict["fixed"] = fixed.map { $0 }
        }
        return dict
    }
}
public func ==(lhs: WithCustomTypesArray, rhs: WithCustomTypesArray) -> Bool {
    return (
        (lhs.ints == nil ? (rhs.ints == nil) : lhs.ints! == rhs.ints!) &&
        (lhs.arrays == nil ? (rhs.arrays == nil) : lhs.arrays! == rhs.arrays!) &&
        (lhs.maps == nil ? (rhs.maps == nil) : lhs.maps! == rhs.maps!) &&
        (lhs.unions == nil ? (rhs.unions == nil) : lhs.unions! == rhs.unions!) &&
        (lhs.fixed == nil ? (rhs.fixed == nil) : lhs.fixed! == rhs.fixed!) &&
        true
    )
}
