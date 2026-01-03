//
//  HorizontalTitleButtonCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 7.02.2025.
//

import UIKit
import ComponentKit

protocol HorizontalTitleButtonCollectionViewCellDelegate: AnyObject {
    func didTappedButton(tag: Int)
}

extension HorizontalTitleButtonCollectionViewCell {
    func didTappedButton(tag: Int) {}
}

final class HorizontalTitleButtonCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var ckHorizontalTitleButtonView: CKHorizontalTitleButtonView = {
        let ckHorizontalTitleButtonView = CKHorizontalTitleButtonView()
        ckHorizontalTitleButtonView.translatesAutoresizingMaskIntoConstraints = false
        return ckHorizontalTitleButtonView
    }()
    
    // MARK: - Members
    private weak var delegate: HorizontalTitleButtonCollectionViewCellDelegate?
    
    // MARK: - Initialize
    override init(frame: CGRect) {
        super.init(frame: frame)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        contentView.addSubview(ckHorizontalTitleButtonView)
        
        NSLayoutConstraint.activate([
            ckHorizontalTitleButtonView.topAnchor.constraint(equalTo: contentView.topAnchor),
            ckHorizontalTitleButtonView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            ckHorizontalTitleButtonView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            ckHorizontalTitleButtonView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: HorizontalTitleButtonCollectionViewCellDelegate? = nil,
              labelText: String = "",
              labelTextColor: UIColor = .clear,
              labelFont: UIFont? = .body04Compact,
              buttonTitle: String = "",
              buttonTitleColor: UIColor = .clear,
              buttonBackgroundColor: UIColor = .clear,
              buttonCornerRadius: CGFloat = 0,
              buttonBorderWidth: CGFloat = 0,
              buttonBorderColor: UIColor = .clear,
              buttonDisabledTextColor: UIColor = .clear,
              buttonDisabledBackgroundColor: UIColor = .clear,
              buttonDisabledBorderColor: UIColor = .clear,
              buttonFont: UIFont? = .bold03Compact,
              buttonIsEnabled: Bool = true,
              buttonImage: UIImage? = nil,
              buttonImageTitleSpacing: CGFloat = 0,
              buttonTag: Int = 0) {
        self.delegate = delegate
        ckHorizontalTitleButtonView.bind(delegate: self,
                                         labelText: labelText,
                                         labelTextColor: labelTextColor,
                                         labelFont: labelFont,
                                         buttonTitle: buttonTitle,
                                         buttonTitleColor: buttonTitleColor,
                                         buttonBackgroundColor: buttonBackgroundColor,
                                         buttonCornerRadius: buttonCornerRadius,
                                         buttonBorderWidth: buttonBorderWidth,
                                         buttonBorderColor: buttonBorderColor,
                                         buttonDisabledTextColor: buttonDisabledTextColor,
                                         buttonDisabledBackgroundColor: buttonDisabledBackgroundColor,
                                         buttonDisabledBorderColor: buttonDisabledBorderColor,
                                         buttonFont: buttonFont,
                                         buttonIsEnabled: buttonIsEnabled,
                                         buttonImage: buttonImage,
                                         buttonImageTitleSpacing: buttonImageTitleSpacing,
                                         buttonTag: buttonTag)
    }
}

// MARK: - CKButtonDelegate
extension HorizontalTitleButtonCollectionViewCell: CKHorizontalTitleButtonViewDelegate {
    func ckHorizontalButtonClicked(tag: Int) {
        delegate?.didTappedButton(tag: tag)
    }
}
