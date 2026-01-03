//
//  TitleButtonAndDescriptionTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 28.05.2025.
//

import UIKit
import ComponentKit

protocol TitleButtonAndDescriptionTableViewCellDelegate: AnyObject {
    func didTappedEditDescriptionButton()
}

extension TitleButtonAndDescriptionTableViewCellDelegate {
    func didTappedEditDescriptionButton() {}
}

final class TitleButtonAndDescriptionTableViewCell: UITableViewCell {
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            textColor: DesignKitColorName.contentStrong900.color,
            numberOfLines: 0,
            font: .body03Compact
        )
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var rightButton: CKButton = {
        let button = CKButton(
            delegate: self,
            titleColor: DesignKitColorName.contentStrong900.color,
            font: .bold03Compact,
            imageTitleSpacing: 4
        )
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(
            textColor: DesignKitColorName.contentSoft600.color,
            numberOfLines: 0,
            font: .body04Compact
        )
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var horizontalStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, distribution: .equalSpacing, spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([titleLabel, rightButton])
        return stackView
    }()
    
    private lazy var verticalStackView: CKStackView = {
        let stackView = CKStackView(spacing: 12)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([horizontalStackView, descriptionLabel])
        return stackView
    }()
    
    // MARK: - Members
    private weak var delegate: TitleButtonAndDescriptionTableViewCellDelegate?
    private var isDescription: Bool = false
    
    // MARK: - Initialize
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        selectionStyle = .none
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        contentView.isUserInteractionEnabled = true
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTappedEditDescriptionButton))
        contentView.addGestureRecognizer(tapGesture)
        
        contentView.addSubview(verticalStackView)
        NSLayoutConstraint.activate([
            verticalStackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            verticalStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            verticalStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            verticalStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: TitleButtonAndDescriptionTableViewCellDelegate? = nil,
              title: String,
              titleColor: UIColor = DesignKitColorName.contentStrong900.color,
              titleFont: UIFont = .body03Compact,
              buttonTitle: String = "",
              buttonImage: UIImage? = nil,
              isHiddenButton: Bool = false,
              description: String = "",
              descriptionColor: UIColor = DesignKitColorName.contentStrong900.color,
              descriptionFont: UIFont = .body04Compact) {
        self.delegate = delegate
        titleLabel.text = title
        titleLabel.textColor = titleColor
        titleLabel.font = titleFont
        
        rightButton.setTitle(buttonTitle)
        rightButton.setImage(buttonImage)
        rightButton.isHidden = isHiddenButton
        
        self.isDescription = !description.isEmpty
        descriptionLabel.isHidden = description.isEmpty
        descriptionLabel.text = description
        descriptionLabel.textColor = descriptionColor
        descriptionLabel.font = descriptionFont
    }
    
    @objc private func didTappedEditDescriptionButton() {
        guard !isDescription else { return }
        delegate?.didTappedEditDescriptionButton()
    }
}

// MARK: - CKButtonDelegate
extension TitleButtonAndDescriptionTableViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        delegate?.didTappedEditDescriptionButton()
    }
}
