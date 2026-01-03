//
//  InfoSwitchTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//

import UIKit
import ComponentKit
import DesignKit

protocol InfoSwitchTableViewCellDelegate: AnyObject {
    func didChangeSwitch(isOn: Bool, tag: Int)
}

extension InfoSwitchTableViewCellDelegate {
    func didChangeSwitch(isOn: Bool, tag: Int) {}
}

final class InfoSwitchTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var switchButton: UISwitch = {
        let switchButton = UISwitch()
        switchButton.onTintColor = DesignKitColorName.contentStrong900.color
        switchButton.translatesAutoresizingMaskIntoConstraints = false
        switchButton.addTarget(self, action: #selector(didChangeSwitch(_:)), for: .valueChanged)
        return switchButton
    }()
    
    private lazy var infoLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .body03Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var subtitleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSoft600.color,
                            numberOfLines: 0,
                            font: .body04Compact)
        return label
    }()
    
    private lazy var titleStackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.addArrangedSubviews([infoLabel, subtitleLabel])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 16)
        stackView.addArrangedSubviews([titleStackView, switchButton])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var separatorView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        view.heightAnchor.constraint(equalToConstant: 1).isActive = true
        return view
    }()
    
    // MARK: - Members
    private weak var delegate: InfoSwitchTableViewCellDelegate?
    
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
        
        contentView.addSubview(stackView)
        contentView.addSubview(separatorView)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
            
            separatorView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            separatorView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            separatorView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func configure(delegate: InfoSwitchTableViewCellDelegate? = nil,
                   info: String,
                   subtitle: String = "",
                   isOn: Bool = false,
                   isSeparator: Bool = true,
                   alignment: UIStackView.Alignment = .center,
                   tag: Int = 0) {
        self.delegate = delegate
        infoLabel.text = info
        subtitleLabel.text = subtitle
        subtitleLabel.isHidden = subtitle.isEmpty
        switchButton.setOn(isOn, animated: false)
        separatorView.isHidden = !isSeparator
        stackView.alignment = alignment
        self.tag = tag
    }
    
    @objc private func didChangeSwitch(_ sender: UISwitch) {
        delegate?.didChangeSwitch(isOn: sender.isOn, tag: self.tag)
    }
}
