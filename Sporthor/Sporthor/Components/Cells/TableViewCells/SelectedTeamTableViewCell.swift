//
//  SelectedTeamTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 25.02.2025.
//

import UIKit
import ComponentKit

// MARK: - SelectedTeamTableViewCellDelegate
protocol SelectedTeamTableViewCellDelegate: AnyObject {
    func selectedTeam(_ model: TeamItemModel)
}

extension SelectedTeamTableViewCellDelegate {
    func selectedTeam(_ model: TeamItemModel) {}
}

final class SelectedTeamTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var teamImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.heightAnchor.constraint(equalToConstant: 48).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 48).isActive = true
        imageView.setCornerRadius(24)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var teamNameLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var selectedButton: CKButton = {
        let button = CKButton(delegate: self, cornerRadius: 15)
        button.widthAnchor.constraint(lessThanOrEqualToConstant: 64).isActive = true
        button.heightAnchor.constraint(equalToConstant: 30).isActive = true
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 8)
        stackView.addArrangedSubviews([teamImageView, teamNameLabel, selectedButton])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Members
    private var model: TeamItemModel?
    private weak var delegate: SelectedTeamTableViewCellDelegate?
    private var stackViewTopConstraint: NSLayoutConstraint!
    private var stackViewBottomConstraint: NSLayoutConstraint!
    
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
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        contentView.addSubview(stackView)
        
        stackViewTopConstraint = stackView.topAnchor.constraint(equalTo: contentView.topAnchor)
        stackViewBottomConstraint = stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        NSLayoutConstraint.activate([
            stackViewTopConstraint,
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 24),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -24),
            stackViewBottomConstraint
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: SelectedTeamTableViewCellDelegate? = nil,
              model: TeamItemModel,
              selectedButtonTitle: String = "Seç",
              topCons: CGFloat = 0,
              bottomCons: CGFloat = 0) {
        stackViewTopConstraint.constant = topCons
        stackViewBottomConstraint.constant = bottomCons
        self.delegate = delegate
        self.model = model
        teamImageView.setImage(with: model.image ?? "")
        teamNameLabel.text = model.name
        selectedButton.setTitle(model.isSelected ? "İptal" : selectedButtonTitle)
        selectedButton.setTitleColor(model.isSelected ? DesignKitColorName.contentStrong900.color : .white)
        selectedButton.setBackgroundColor(model.isSelected ? DesignKitColorName.backgroundPrimaryGreen.color : DesignKitColorName.contentStrong900.color)
    }
}

// MARK: - CKButtonDelegate
extension SelectedTeamTableViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        guard let delegate, let model else { return }
        delegate.selectedTeam(model)
    }
}
