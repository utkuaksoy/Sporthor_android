//
//  Extension+UILabel.swift
//  ComponentKit
//
//  Created by derTurke on 26.04.2025.
//

import UIKit

public extension UILabel {
    func detectTap(on substring: String, gesture: UITapGestureRecognizer, completion: @escaping () -> Void) {
        guard let attributedText = self.attributedText else { return }
        let text = attributedText.string
        let range = (text as NSString).range(of: substring)
        guard range.location != NSNotFound else { return }

        let layoutManager = NSLayoutManager()
        let textContainer = NSTextContainer(size: bounds.size)
        textContainer.lineFragmentPadding = 0
        textContainer.maximumNumberOfLines = numberOfLines
        textContainer.lineBreakMode = lineBreakMode

        let textStorage = NSTextStorage(attributedString: attributedText)
        textStorage.addLayoutManager(layoutManager)
        layoutManager.addTextContainer(textContainer)

        let location = gesture.location(in: self)
        let characterIndex = layoutManager.characterIndex(for: location, in: textContainer, fractionOfDistanceBetweenInsertionPoints: nil)

        if NSLocationInRange(characterIndex, range) {
            completion()
        }
    }
}
