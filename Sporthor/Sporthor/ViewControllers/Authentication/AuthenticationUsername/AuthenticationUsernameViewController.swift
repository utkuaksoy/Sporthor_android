//
//  AuthenticationUsernameViewController.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import UIKit
import ComponentKit

final class AuthenticationUsernameViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: AuthenticationUsernamePresenterProtocol {
        get { return self.basePresenter as! AuthenticationUsernamePresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var textStackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color, numberOfLines: 0, font: .heading04)
        return label
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color, numberOfLines: 0, font: .body04Compact)
        return label
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.contentInset = UIEdgeInsets(top: 32, left: 0, bottom: 0, right: 0)
        tableView.separatorStyle = .none
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.backgroundColor = .clear
        return tableView
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(
            delegate: self,
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            disabledTextColor: DesignKitColorName.contentSoft600.color,
            disabledBackgroundColor: DesignKitColorName.backgroundSub300.color,
            font: .bold03Compact,
            isEnabled: false,
            tag: 0)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - AuthenticationPersonalInformationPresenterDelegate
extension AuthenticationUsernameViewController: AuthenticationUsernamePresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String) {
        titleLabel.text = title
        descriptionLabel.text = description
    }
    
    func updateContinueButtonTitle(_ title: String) {
        continueButton.setTitle(title)
    }
    
    func continueButtonEnabled(isEnabled: Bool) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.continueButton.setEnabled(isEnabled)
        }
    }
    
    func prepareUI() {
        textStackView.addArrangedSubviews([titleLabel, descriptionLabel])
        view.addSubview(textStackView)
        view.addSubview(continueButton)
        view.addSubview(tableView)
        layoutConstraintActive()
    }
    
    private func layoutConstraintActive() {
        NSLayoutConstraint.activate([
            textStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 24),
            textStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            textStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            continueButton.leadingAnchor.constraint(equalTo: textStackView.leadingAnchor),
            continueButton.trailingAnchor.constraint(equalTo: textStackView.trailingAnchor),
            continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -50),
            continueButton.heightAnchor.constraint(equalToConstant: 46),
            tableView.topAnchor.constraint(equalTo: textStackView.bottomAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: continueButton.topAnchor)
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
    
    func didSetFocusTextField(at indexPath: IndexPath) {
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) { [weak self] in
            guard let self else { return }
            guard let nextCell = self.tableView.cellForRow(at: indexPath) as? TextFieldTableViewCell else { return }
            nextCell.textFieldBecomeFirstResponder()
        }
    }
}

// MARK: - UITableViewDataSource
extension AuthenticationUsernameViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return presenter.isValidateUsername ? 1 : 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = TextFieldTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      titleText: DesignKitL10n.Authentication.Username.title,
                      textFieldText: presenter.username,
                      textFieldPlaceholder: DesignKitL10n.Authentication.Username.title,
                      textFieldBorderColor: presenter.isValidateUsername ? .clear : DesignKitColorName.red500.color,
                      textFieldCapitalizationType: .none,
                      textFieldTag: 0,
                      statusImage: presenter.isTrueUsername ?  UIImage(named: "success-alert") :  UIImage(named: "error-alert"),
                      statusImageWidth: 18,
                      statusImageHeight: 18,
                      statusDescriptionText: presenter.alertDescription,
                      statusDescriptionFont: .body04Compact,
                      indexPath: indexPath,
                      leadingAnchorCons: 24,
                      trailingAnchorCons: -24)
            return cell
        case 1:
            let cell = HorizontalUsernameTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      data: presenter.checkUsernameResponse?.suggestions ?? [],
                      leadingCons: 24)
            return cell
        default:
            let cell = UITableViewCell()
            cell.backgroundColor = .clear
            return cell
        }
    }
}

// MARK: - UITableViewDelegate
extension AuthenticationUsernameViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return indexPath.section == 1 ? 34 : UITableView.automaticDimension
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let view = CKHeaderView(text: section == 1 ? DesignKitL10n.Authentication.Username.suggestionHeader : "",
                                leadingCons: 24,
                                trailingCons: -24)
        return view
        
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        guard section != 1 else { return 24 }
        return 0
    }
}

// MARK: - TextFieldTableViewCellDelegate
extension AuthenticationUsernameViewController: TextFieldTableViewCellDelegate {
    func textFieldDidChangeSelection(text: String, tag: Int, indexPath: IndexPath?) {
        guard let indexPath else { return }
        presenter.textFieldDidChangeSelection(text: text, tag: tag, indexPath: indexPath)
    }
    
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?) {
        guard let indexPath else { return }
        presenter.textFieldDidEndEditing(text: text, tag: tag, indexPath: indexPath)
    }
}

// MARK: - HorizontalUsernameTableViewCellDelegate
extension AuthenticationUsernameViewController: HorizontalUsernameTableViewCellDelegate {
    func didSelectItem(_ text: String) {
        presenter.didSelectUsername(text)
    }
}

// MARK: - CKButtonDelegate
extension AuthenticationUsernameViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedContinueButton()
    }
}
