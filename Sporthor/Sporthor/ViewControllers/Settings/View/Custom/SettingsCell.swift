//
//  SettingsCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import DesignKit
import UIKit

final class SettingsCell: UITableViewCell {
    // MARK: - Properties
    static let identifier = "SettingsCell"
    
    // MARK: - Private UI Elements

    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.font = .bold03Compact
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    // MARK: - Initialize
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        iconImageView.image = nil
        titleLabel.text = nil
        titleLabel.textColor = .label
        iconImageView.tintColor = .label
    }

    // MARK: - Configure
    func configure(with item: SettingsItem) {
        titleLabel.text = item.title
        iconImageView.image = item.icon
        
        if item.isDestructive {
            titleLabel.textColor = ColorName.red600.color
            iconImageView.tintColor = ColorName.red600.color
        } else {
            titleLabel.textColor = ColorName.contentStrong900.color
            iconImageView.tintColor = ColorName.contentStrong900.color
        }
    }
    
    func configure(with postSettingItem: PostSettingItems) {
        titleLabel.text = postSettingItem.title
        iconImageView.image = postSettingItem.icon
        
        if postSettingItem.isDestructive {
            titleLabel.textColor = ColorName.red600.color
            iconImageView.tintColor = ColorName.red600.color
        } else {
            titleLabel.textColor = ColorName.contentStrong900.color
            iconImageView.tintColor = ColorName.contentStrong900.color
        }
    }
    
    func configure(with profileSettingItem: ProfileSettingItems) {
        titleLabel.text = profileSettingItem.title
        iconImageView.image = profileSettingItem.icon
        
        if profileSettingItem.isDestructive {
            titleLabel.textColor = ColorName.red600.color
            iconImageView.tintColor = ColorName.red600.color
        } else {
            titleLabel.textColor = ColorName.contentStrong900.color
            iconImageView.tintColor = ColorName.contentStrong900.color
        }
    }
    
    func configure(with storySettingItem: StorySettingItems,
                   isDark: Bool = false) {
        contentView.backgroundColor = isDark ? ColorName.contentStrong900.color : DesignKitColorName.backgroundWeak100.color
        titleLabel.text = storySettingItem.title
        iconImageView.image = storySettingItem.icon
        
        if storySettingItem.isDestructive {
            titleLabel.textColor = ColorName.red600.color
            iconImageView.tintColor = ColorName.red600.color
        } else {
            titleLabel.textColor = isDark ? DesignKitColorName.backgroundWeak100.color : ColorName.contentStrong900.color
            iconImageView.tintColor = isDark ? DesignKitColorName.backgroundWeak100.color : ColorName.contentStrong900.color
        }
    }
}

// MARK: - Setup

private extension SettingsCell {
    
    func setupViews() {
        backgroundColor = .clear
        selectionStyle = .none
        
        contentView.addSubview(iconImageView)
        contentView.addSubview(titleLabel)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            iconImageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            iconImageView.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            iconImageView.widthAnchor.constraint(equalToConstant: 24),
            iconImageView.heightAnchor.constraint(equalToConstant: 24),
            
            titleLabel.leadingAnchor.constraint(equalTo: iconImageView.trailingAnchor, constant: 16),
            titleLabel.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            titleLabel.centerYAnchor.constraint(equalTo: contentView.centerYAnchor)
        ])
    }
}
