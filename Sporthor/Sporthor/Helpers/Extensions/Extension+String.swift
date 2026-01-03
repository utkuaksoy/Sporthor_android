//
//  Extension+String.swift
//  Sporthor
//
//  Created by derTurke.
//

import UIKit

extension String {
    func width(withConstrainedHeight height: CGFloat, font: UIFont?) -> CGFloat {
        let constraintRect = CGSize(width: .greatestFiniteMagnitude, height: height)
        let boundingBox = self.boundingRect(
            with: constraintRect,
            options: .usesLineFragmentOrigin,
            attributes: [.font: font ?? .systemFont(ofSize: 14)],
            context: nil
        )
        return ceil(boundingBox.width)
    }
    
    func getLabelHeight(
        width: CGFloat,
        font: UIFont,
        numberOfLines: Int = 0,
        lineBreakMode: NSLineBreakMode = .byWordWrapping
    ) -> CGFloat {
        let label = UILabel(frame: .zero)
        label.frame.size.width = width
        label.numberOfLines = numberOfLines
        label.lineBreakMode = lineBreakMode
        label.font = font
        label.text = self
        label.sizeToFit()
        
        return label.frame.size.height
    }
    
    func formatPhoneNumber() -> String {
        let cleanPhoneNumber = self.replacingOccurrences(of: "[^0-9]", with: "", options: .regularExpression)
        
        guard cleanPhoneNumber.count == 10 else {
            return self
        }
        
        let firstPart = cleanPhoneNumber.prefix(3)
        let secondPart = cleanPhoneNumber.dropFirst(3).prefix(3)
        let thirdPart = cleanPhoneNumber.dropFirst(6).prefix(2)
        let fourthPart = cleanPhoneNumber.dropFirst(8)
        
        return "\(firstPart) \(secondPart) \(thirdPart) \(fourthPart)"
    }

    func removePhoneNumberFormatting() -> String {
        return self.replacingOccurrences(of: "[^0-9]", with: "", options: .regularExpression)
    }
    
    func masked(_ maskingCharacter: String,
                started: Int? = nil,
                finished: Int? = nil,
                skippedCharacters: String? = nil) -> String {
        let charactersToSkip = Set(skippedCharacters ?? "")
        var characters = Array(self)
        
        if let start = started, start < characters.count {
            let end = finished ?? characters.count
            
            if end > start && end <= characters.count {
                for i in start..<end {
                    if !charactersToSkip.contains(characters[i]) {
                        characters[i] = Character(maskingCharacter)
                    }
                }
            }
        }
        
        return String(characters)
    }
    
    func isValidEmail() -> Bool {
        let emailRegex = "^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegex)
        return emailTest.evaluate(with: self)
    }
    
    func isValidPassword() -> Bool {
        let passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$"
        return NSPredicate(format: "SELF MATCHES %@", passwordRegex).evaluate(with: self)
    }
    
    func toDate(_ format: String = "dd.MM.yyyy") -> Date? {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = format
        return dateFormatter.date(from: self) ?? nil
    }
    
    func isValidateUsername() -> Bool {
        let usernameRegex = "^[a-z0-9._]{4,15}$"
        return NSPredicate(format: "SELF MATCHES %@", usernameRegex).evaluate(with: self)
    }
    
    func formatDateField(seperator: Character = ".") -> String {
        let dateRegex = replacingOccurrences(of: "[^0-9]", with: "", options: .regularExpression)
        var formattedText = ""

        for (index, char) in dateRegex.enumerated() {
            if index == 2 || index == 4 {
                formattedText.append(seperator)
            }
            if index < 8 {
                formattedText.append(char)
            }
        }

        return formattedText
    }
}
