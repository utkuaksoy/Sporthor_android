//
//  ActionCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 26.03.2025.
//

import ComponentKit
import DesignKit
import UIKit

protocol ActionCellDelegate: AnyObject {
    func didTapAction(item: ActionItemEnum)
}

final class ActionCell: UITableViewCell {
    
    static let reuseIdentifier = "ActionCell"
    
    private let iconImageView: UIImageView = {
        let iv = UIImageView()
        iv.contentMode = .scaleAspectFit
        iv.tintColor = .black
        iv.translatesAutoresizingMaskIntoConstraints = false
        iv.widthAnchor.constraint(equalToConstant: 24).isActive = true
        iv.heightAnchor.constraint(equalToConstant: 24).isActive = true
        return iv
    }()
    
    private let titleLabel: UILabel = {
        let label = UILabel()
        label.font = .body04Compact
        label.textColor = ColorName.contentStrong900.color
        return label
    }()
    
    private let badgeLabel: UILabel = {
        let label = UILabel()
        label.text = "Önerilir"
        label.font = .body06MediumCompact
        label.textColor = ColorName.pink800.color
        label.backgroundColor = ColorName.pink100.color
        label.layer.cornerRadius = 10
        label.clipsToBounds = true
        label.textAlignment = .center
        label.isHidden = true
        label.setContentHuggingPriority(.required, for: .horizontal)
        label.setContentCompressionResistancePriority(.required, for: .horizontal)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var rightChevron: UIImageView = {
        let iv = UIImageView(image: UIImage(systemName: "chevron.right"))
        iv.tintColor = ColorName.contentStrong900.color
        return iv
    }()
    
    private lazy var titleStack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [titleLabel, badgeLabel])
        stack.axis = .horizontal
        stack.spacing = 8
        stack.alignment = .center
        return stack
    }()
    
    private lazy var mainStack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [iconImageView, titleStack, rightChevron])
        stack.axis = .horizontal
        stack.spacing = 12
        stack.alignment = .center
        stack.translatesAutoresizingMaskIntoConstraints = false
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.didTapAction(_:)))
        stack.addGestureRecognizer(tap)
        return stack
    }()
    
    // MARK: - Private Properties

    private weak var delegate: ActionCellDelegate?
    private var currentActionItem: ActionItemEnum?
    
    // MARK: Init

    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        selectionStyle = .none
        contentView.addSubview(mainStack)
        
        NSLayoutConstraint.activate([
            mainStack.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 12),
            mainStack.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -12),
            mainStack.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            mainStack.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            
            badgeLabel.heightAnchor.constraint(equalToConstant: 24),
            badgeLabel.widthAnchor.constraint(greaterThanOrEqualToConstant: 64)
        ])
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func configure(delegate: ActionCellDelegate?, item: ActionItemEnum?) {
        self.delegate = delegate
        self.currentActionItem = item
        titleLabel.text = item?.title
        iconImageView.image = item?.image
        badgeLabel.isHidden = item?.isBadgeHidden ?? true
    }
    
    @objc
    private func didTapAction(_ sender: UITapGestureRecognizer) {
        guard let currentActionItem else { return }
        self.delegate?.didTapAction(item: currentActionItem)
    }
}
