//
//  MenuTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//

import UIKit
import ComponentKit

protocol MenuTableViewCellDelegate: AnyObject {
    func updates()
    func didTappedMainMenu(_ model: MenuModel)
    func didTappedSubMenu(_ model: MenuModel)
    func didTappedMenu(tag: Int)
}

extension MenuTableViewCellDelegate {
    func updates() {}
    func didTappedMainMenu(_ model: MenuModel) {}
    func didTappedSubMenu(_ model: MenuModel) {}
    func didTappedMenu(tag: Int) {}
}

final class MenuTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFit
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            textColor: DesignKitColorName.contentStrong900.color,
            numberOfLines: 0,
            font: .bold03Compact
        )
        return label
    }()
    
    private lazy var rightIconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()
    
    private lazy var menuStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal,
                                    alignment: .center,
                                    spacing: 8)
        stackView.addArrangedSubviews([iconImageView,
                                       titleLabel,
                                       rightIconImageView])
        stackView.isUserInteractionEnabled = true
        stackView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(didTappedMenu)))
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var subMenuStackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var contentStackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.addArrangedSubviews([menuStackView, subMenuStackView])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Members
    private weak var delegate: MenuTableViewCellDelegate?
    private var model: MenuModel?
    private var isOpenDetail: Bool = false
    private var contentStackViewLeadingCons: NSLayoutConstraint!
    private var contentStackViewTrailingCons: NSLayoutConstraint!
    
    // MARK: - Initializers
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
        contentView.addSubview(contentStackView)
        
        contentStackViewLeadingCons = contentStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 24)
        contentStackViewTrailingCons = contentStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -24)
        
        NSLayoutConstraint.activate([
            contentStackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            contentStackViewLeadingCons,
            contentStackViewTrailingCons,
            contentStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16)
        ])
    }
    
    // MARK: - Custom Methods
    func configure(delegate: MenuTableViewCellDelegate? = nil, title: String, tag: Int) {
        self.delegate = delegate
        iconImageView.isHidden = true
        titleLabel.text = title
        rightIconImageView.image = Asset.chevronRightGrey.image
        contentStackViewLeadingCons.constant = 16
        contentStackViewTrailingCons.constant = -16
        self.tag = tag
    }
    
    func configure(delegate: MenuTableViewCellDelegate? = nil, model: MenuModel) {
        self.delegate = delegate
        self.model = model
        iconImageView.kf.setImage(with: URL(string: model.iconPath))
        titleLabel.text = model.name
        rightIconImageView.isHidden = model.subMenus.isEmpty
        rightIconImageView.image = isOpenDetail ? Asset.chevronUpGrey.image : Asset.chevronRightGrey.image
        if !model.mainMenu {
            setupSubMenuStackView(subMenu: model.subMenus)
        } else {
            subMenuStackView.isHidden = true
        }
    }
    
    private func setupSubMenuStackView(subMenu: [MenuModel]) {
        subMenuStackView.isHidden = true
        subMenuStackView.removeAllArrangedSubviews()
        
        subMenu.forEach {
            let view = SubMenuCardView()
            view.bind(delegate: self, model: $0)
            view.heightAnchor.constraint(equalToConstant: 56).isActive = true
            subMenuStackView.addArrangedSubview(view)
        }
    }
    
    @objc private func didTappedMenu() {
        if let model {
            if !model.mainMenu {
                isOpenDetail = !isOpenDetail
                subMenuStackView.isHidden = !isOpenDetail
                rightIconImageView.image = isOpenDetail ? Asset.chevronUpGrey.image : Asset.chevronRightGrey.image
                if model.subMenus.isEmpty {
                    delegate?.didTappedMainMenu(model)
                } else {
                    delegate?.updates()
                }
            } else {
                delegate?.didTappedMainMenu(model)
            }
        } else {
            delegate?.didTappedMenu(tag: tag)
        }
    }
}

extension MenuTableViewCell: SubMenuCardViewDelegate {
    func didTappedSubMenu(_ model: MenuModel) {
        delegate?.didTappedSubMenu(model)
    }
}
