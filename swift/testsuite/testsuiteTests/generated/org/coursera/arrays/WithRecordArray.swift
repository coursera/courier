import Foundation
import SwiftyJSON

struct WithRecordArray: JSONSerializable, Equatable {
    
    let empties: [Empty]?
    
    let fruits: [Fruits]?
    
    init(
        empties: [Empty]?,
        fruits: [Fruits]?
    ) {
        self.empties = empties
        self.fruits = fruits
    }
    
    static func read(json: JSON) -> WithRecordArray {
        return WithRecordArray(
            empties: json["empties"].array.map { $0.map { Empty.read($0.jsonValue) } },
            fruits: json["fruits"].array.map { $0.map { Fruits.read($0.stringValue) } }
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let empties = self.empties {
            json["empties"] = JSON(empties.map { $0.write() })
        }
        if let fruits = self.fruits {
            json["fruits"] = JSON(fruits.map { JSON($0.write()) })
        }
        return JSON(json)
    }
}
func ==(lhs: WithRecordArray, rhs: WithRecordArray) -> Bool {
    return (
        (lhs.empties == nil ? (rhs.empties == nil) : lhs.empties! == rhs.empties!) &&
        (lhs.fruits == nil ? (rhs.fruits == nil) : lhs.fruits! == rhs.fruits!) &&
        true
    )
}
