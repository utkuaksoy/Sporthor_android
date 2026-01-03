//
//  RepeatTaskTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//

import UIKit
import ComponentKit
import DesignKit

protocol RepeatTaskTableViewCellDelegate: AnyObject {
    func didTappedSelectRepeatTask(tag: Int)
    func didChangeSwitchRepeatTask(isOn: Bool, tag: Int)
    func didTappedDateRepeatTaskTableViewCell(_ date: String)
}

extension RepeatTaskTableViewCellDelegate {
    func didTappedSelectRepeatTask(tag: Int) {}
    func didChangeSwitchRepeatTask(isOn: Bool, tag: Int) {}
    func didTappedDateRepeatTaskTableViewCell(_ date: String) {}
}

final class RepeatTaskTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var baseView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundWeak100.color
        view.setCornerRadius(8)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
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
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 16)
        stackView.addArrangedSubviews([infoLabel, switchButton])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var buttonStackView: CKWrappingStackView = {
        let view = CKWrappingStackView()
        view.spacing = 8
        view.lineSpacing = 8
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var seperatorView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.borderSoft200.color
        view.heightAnchor.constraint(equalToConstant: 1).isActive = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var repeatTitleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .body03Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var repeatDateLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            textAlignment: .center,
                            font: .body04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var repeatDateView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = DesignKitColorName.backgroundWhite0.color
        view.setCornerRadius(8)
        view.isUserInteractionEnabled = true
        view.heightAnchor.constraint(equalToConstant: 38).isActive = true
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTappedDate))
        view.addGestureRecognizer(tapGesture)
        return view
    }()
    
    private lazy var dateStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center)
        stackView.addArrangedSubviews([repeatTitleLabel, repeatDateView])
        return stackView
    }()
    
    private lazy var bottomDateStackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.addArrangedSubviews([seperatorView, dateStackView])
        return stackView
    }()
    
    private lazy var bottomStackView: CKStackView = {
        let stackView = CKStackView(spacing: 16)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([buttonStackView, bottomDateStackView])
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = .init(top: 0, left: 16, bottom: 0, right: 16)
        return stackView
    }()
    
    // MARK: - Members
    private weak var delegate: RepeatTaskTableViewCellDelegate?
    private var stackViewBottomCons: NSLayoutConstraint!
    private var repeatDate: String = ""
    
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
        
        repeatDateView.addSubview(repeatDateLabel)
        baseView.addSubview(stackView)
        baseView.addSubview(bottomStackView)
        contentView.addSubview(baseView)
        stackViewBottomCons = stackView.bottomAnchor.constraint(equalTo: baseView.bottomAnchor, constant: -8)
        
        NSLayoutConstraint.activate([
            repeatDateLabel.topAnchor.constraint(equalTo: repeatDateView.topAnchor, constant: 8),
            repeatDateLabel.leadingAnchor.constraint(equalTo: repeatDateView.leadingAnchor, constant: 16),
            repeatDateLabel.trailingAnchor.constraint(equalTo: repeatDateView.trailingAnchor, constant: -16),
            repeatDateLabel.bottomAnchor.constraint(equalTo: repeatDateView.bottomAnchor, constant: -8),
            
            baseView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            baseView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            baseView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            baseView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16),
            
            stackView.topAnchor.constraint(equalTo: baseView.topAnchor, constant: 8),
            stackView.leadingAnchor.constraint(equalTo: baseView.leadingAnchor, constant: 16),
            stackView.trailingAnchor.constraint(equalTo: baseView.trailingAnchor, constant: -16),
            stackViewBottomCons,
            
            bottomStackView.topAnchor.constraint(equalTo: stackView.bottomAnchor, constant: 16),
            bottomStackView.leadingAnchor.constraint(equalTo: baseView.leadingAnchor),
            bottomStackView.trailingAnchor.constraint(equalTo: baseView.trailingAnchor),
            bottomStackView.bottomAnchor.constraint(equalTo: baseView.bottomAnchor, constant: -16),
        ])
    }
    
    // MARK: - Custom Methods
    func configure(delegate: RepeatTaskTableViewCellDelegate? = nil,
                   info: String,
                   isOn: Bool = false,
                   buttons: [RepeatTaskTimeModel] = [],
                   isButtonsHidden: Bool = false,
                   repeatTitle: String = "",
                   repeatDate: String = "",
                   tag: Int = 0) {
        self.delegate = delegate
        self.repeatDate = repeatDate
        infoLabel.text = info
        switchButton.setOn(isOn, animated: true)
        
        bottomStackView.isHidden = isButtonsHidden
        stackViewBottomCons.isActive = isButtonsHidden
        
        setupButtons(buttons: buttons)
        repeatTitleLabel.text = repeatTitle
        repeatDateLabel.text = repeatDate
        
        self.tag = tag
    }
    
    private func setupButtons(buttons: [RepeatTaskTimeModel]) {
        buttonStackView.arrangedSubviews.forEach {
            buttonStackView.removeArrangeSubview($0)
        }
        
        for item in buttons {
            let button = CKButton(delegate: self,
                                  title: item.type.title,
                                  titleColor: item.isSelected ? .white : DesignKitColorName.contentStrong900.color,
                                  buttonBackgroundColor: item.isSelected ? DesignKitColorName.contentStrong900.color : .white,
                                  cornerRadius: 16,
                                  borderWidth: item.isSelected ? 0 : 1,
                                  borderColor: item.isSelected ? .clear : DesignKitColorName.borderSoft200.color,
                                  font: .body04Compact,
                                  tag: item.type.rawValue)
            button.contentEdgeInsets = UIEdgeInsets(top: 8, left: 12, bottom: 8, right: 12)
            buttonStackView.addArrangedSubview(button)
        }
    }
    
    @objc private func didChangeSwitch(_ sender: UISwitch) {
        delegate?.didChangeSwitchRepeatTask(isOn: sender.isOn, tag: self.tag)
    }
    
    @objc private func didTappedDate() {
        delegate?.didTappedDateRepeatTaskTableViewCell(repeatDate)
    }
}

extension RepeatTaskTableViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        delegate?.didTappedSelectRepeatTask(tag: tag)
    }
}
