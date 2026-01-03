//
//  CKSegmentedControl.swift
//  ComponentKit
//
//  Created by GÜRHAN YUVARLAK on 31.10.2025.
//

import UIKit
import DesignKit

public protocol CKSegmentedControlDelegate: AnyObject {
    func segmentedControl(_ control: CKSegmentedControl, didSelect index: Int)
}

public final class CKSegmentedControl: UIView {
    
    // MARK: - UI Elements
    private lazy var stackView: CKStackView = {
        let stack = CKStackView(axis: .horizontal, distribution: .fillEqually)
        stack.translatesAutoresizingMaskIntoConstraints = false
        stack.heightAnchor.constraint(equalToConstant: 40).isActive = true
        return stack
    }()
    
    private lazy var underlineView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = ColorName.contentStrong900.color
        return view
    }()
    
    private lazy var separatorView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = ColorName.borderSoft200.color
        view.heightAnchor.constraint(equalToConstant: 1).isActive = true
        return view
    }()
    
    private lazy var contentStackView: CKStackView = {
        let stack = CKStackView(axis: .vertical)
        stack.translatesAutoresizingMaskIntoConstraints = false
        stack.addArrangedSubviews([stackView, separatorView])
        return stack
    }()
    
    // MARK: - Members
    private var buttons: [UIButton] = []
    private var selectedIndex: Int = 0
    private var underlineLeadingConstraint: NSLayoutConstraint?
    private var underlineWidthConstraint: NSLayoutConstraint?
    
    public weak var delegate: CKSegmentedControlDelegate?
    
    // MARK: - Initializers
    public init(delegate: CKSegmentedControlDelegate?,
                titles: [String],
                selectedIndex: Int = 0) {
        super.init(frame: .zero)
        setupView()
        bind(delegate: delegate, titles: titles, selectedIndex: selectedIndex)
    }
    
    public required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }
    
    // MARK: - Setup
    private func setupView() {
        addSubview(contentStackView)
        addSubview(underlineView)
        
        NSLayoutConstraint.activate([
            contentStackView.topAnchor.constraint(equalTo: topAnchor),
            contentStackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            contentStackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            contentStackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    // MARK: - Public API
    public func bind(delegate: CKSegmentedControlDelegate?,
                     titles: [String],
                     selectedIndex: Int = 0) {
        self.delegate = delegate
        buttons.forEach { $0.removeFromSuperview() }
        buttons = []
        
        for (index, title) in titles.enumerated() {
            let button = CKButton(title: title,
                                  titleColor: ColorName.contentSoft600.color,
                                  font: .bold03Compact,
                                  tag: index)
            button.addTarget(self, action: #selector(buttonTapped(_:)), for: .touchUpInside)
            stackView.addArrangedSubview(button)
            buttons.append(button)
        }
        
        self.selectedIndex = selectedIndex
        layoutIfNeeded() // frame’ler oluşsun
        setupUnderlineConstraints()
        updateSelection(animated: false)
    }
    
    // MARK: - Underline
    private func setupUnderlineConstraints() {
        guard selectedIndex < buttons.count else { return }
        let selectedButton = buttons[selectedIndex]
        
        underlineLeadingConstraint?.isActive = false
        underlineWidthConstraint?.isActive = false
        
        underlineLeadingConstraint = underlineView.leadingAnchor.constraint(equalTo: selectedButton.leadingAnchor)
        underlineWidthConstraint = underlineView.widthAnchor.constraint(equalTo: selectedButton.widthAnchor)
        
        NSLayoutConstraint.activate([
            underlineLeadingConstraint!,
            underlineWidthConstraint!,
            underlineView.topAnchor.constraint(equalTo: stackView.bottomAnchor),
            underlineView.heightAnchor.constraint(equalToConstant: 3)
        ])
    }
    
    // MARK: - Actions
    @objc private func buttonTapped(_ sender: CKButton) {
        selectedIndex = sender.tag
        updateSelection(animated: true)
        delegate?.segmentedControl(self, didSelect: selectedIndex)
    }
    
    // MARK: - UI Update
    private func updateSelection(animated: Bool) {
        for (i, button) in buttons.enumerated() {
            button.setTitleColor(
                i == selectedIndex ? ColorName.contentStrong900.color : ColorName.contentSoft600.color,
                for: .normal
            )
        }
        
        guard selectedIndex < buttons.count else { return }
        let selectedButton = buttons[selectedIndex]
        
        underlineLeadingConstraint?.isActive = false
        underlineWidthConstraint?.isActive = false
        
        underlineLeadingConstraint = underlineView.leadingAnchor.constraint(equalTo: selectedButton.leadingAnchor)
        underlineWidthConstraint = underlineView.widthAnchor.constraint(equalTo: selectedButton.widthAnchor)
        
        underlineLeadingConstraint?.isActive = true
        underlineWidthConstraint?.isActive = true
        
        if animated {
            UIView.animate(withDuration: 0.25) {
                self.layoutIfNeeded()
            }
        } else {
            layoutIfNeeded()
        }
    }
    
    public override func layoutSubviews() {
        super.layoutSubviews()
        updateSelection(animated: false)
    }
}
