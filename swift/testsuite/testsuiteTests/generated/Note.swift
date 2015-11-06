import Foundation
import SwiftyJSON

public struct Note: JSONSerializable, DataTreeSerializable, Equatable {
    
    public let text: String?
    
    public init(
        text: String?
    ) {
        self.text = text
    }
    
    public static func readJSON(json: JSON) throws -> Note {
        return Note(
            text: json["text"].string
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> Note {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let text = self.text {
            dict["text"] = text
        }
        return dict
    }
}
public func ==(lhs: Note, rhs: Note) -> Bool {
    return (
        (lhs.text == nil ? (rhs.text == nil) : lhs.text! == rhs.text!) &&
        true
    )
}
