//
//  CalendarAddMissionViewController.swift
//  Sporthor
//
//  Created by derTurke on 22.05.2025.
//
//

import UIKit
import ComponentKit

final class CalendarAddMissionViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CalendarAddMissionPresenterProtocol {
        get { return self.basePresenter as! CalendarAddMissionPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var tableView: UITableView = {
        let tableView = UITableView(frame: .zero, style: .grouped)
        tableView.delegate = self
        tableView.dataSource = self
        tableView.allowsSelection = false
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.removeEmptyCell()
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 12, right: 0)
        tableView.separatorStyle = .none
        tableView.backgroundColor = .clear
        return tableView
    }()
    
    private lazy var submitButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: "Gönder",
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            font: .bold03Compact
        )
        button.translatesAutoresizingMaskIntoConstraints = false
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.navigationItem.setHidesBackButton(true, animated: false)
        if let navCon = navigationController as? CustomNavigationController {
            let appearance = UINavigationBarAppearance()
            appearance.configureWithTransparentBackground()
            appearance.backgroundColor = .clear
            appearance.shadowColor = .clear
            appearance.titleTextAttributes = [
                .foregroundColor: DesignKitColorName.contentStrong900.color,
                .font: UIFont.bold03Compact
            ]
            
            let navigationBar = navCon.navigationBar
            navigationBar.standardAppearance = appearance
            navigationBar.scrollEdgeAppearance = appearance
            navigationBar.compactAppearance = appearance
            navCon.customDelegate = self
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        navigationController?.navigationItem.setHidesBackButton(false, animated: false)
    }
    
    // MARK: - Custom Methods
}

// MARK: - CalendarAddMissionPresenterDelegate
extension CalendarAddMissionViewController: CalendarAddMissionPresenterDelegate {
    func prepareNavigationBar() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isBackChevronLeft = true
        }
    }
    
    func prepareUI() {
        view.addSubview(tableView)
        view.addSubview(submitButton)
        NSLayoutConstraint.activate([
            submitButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32),
            submitButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            submitButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: submitButton.topAnchor)
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
    
    func didChangeSubmitButtonTitle(_ title: String) {
        submitButton.setTitle(title)
    }
}

// MARK: - CustomNavigationControllerDelegate
extension CalendarAddMissionViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.didTappedNavigationButton(type)
    }
}

extension CalendarAddMissionViewController: UITableViewDelegate, UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return presenter.isDraftEdit || presenter.isEdit ? 6 : 7
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 1:
            return 4
        default:
            return 1
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = TextFieldTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      textFieldText: presenter.headerTitle,
                      textFieldPlaceholder: "Etkinlik Başlığı",
                      textFieldPlaceholderColor: DesignKitColorName.contentDisable300.color,
                      textFieldBackgroundColor: .clear,
                      textFieldSelectedBorderColor: .clear,
                      textFieldFont: .heading04,
                      textFieldMaxLength: 100,
                      textFieldTag: 1)
            return cell
        case 1:
            switch indexPath.row {
            case 0:
                let cell = InfoSwitchTableViewCell.dequeue(from: tableView, at: indexPath)
                cell.configure(delegate: self, info: "Tam Gün", isOn: presenter.isFullDay)
                return cell
            case 1:
                let cell = TitleDateAndHourTableViewCell.dequeue(from: tableView, at: indexPath)
                cell.configure(delegate: self,
                               type: .start,
                               title: "Başlangıç",
                               date: presenter.startDate.toString("dd MMMM yyyy"),
                               hour: presenter.startHour,
                               isHiddenHourView: presenter.isFullDay)
                return cell
            case 2:
                let cell = TitleDateAndHourTableViewCell.dequeue(from: tableView, at: indexPath)
                cell.configure(delegate: self,
                               type: .end,
                               title: "Bitiş",
                               date: presenter.endDate.toString("dd MMMM yyyy"),
                               hour: presenter.endHour,
                               isHiddenHourView: presenter.isFullDay)
                return cell
            case 3:
                let cell = RepeatTaskTableViewCell.dequeue(from: tableView, at: indexPath)
                cell.configure(
                    delegate: self,
                    info: "Etkinliği Tekrarla",
                    isOn: presenter.isRepeat,
                    buttons: presenter.repeatTaskTime,
                    isButtonsHidden: !presenter.isRepeat,
                    repeatTitle: "Son Tekrarlanma Tarihi",
                    repeatDate: presenter.endDate.toString("dd MMMM yyyy")
                )
                return cell
            default:
                return UITableViewCell()
            }
        case 2:
            let cell = EventTypeTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.configure(delegate: self,
                           model: presenter.eventTypes,
                           info: "Aradığını bulamadın mı? Kendi etkinliğinizi tanımlayın.")
            return cell
        case 3:
            let cell = CommunityTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      leftImage: Asset.userPlus.image,
                      title: "Kişi / Topluluk Ekle",
                      profileImages: presenter.profileImages,
                      rightImage: Asset.chevronRight.image)
            return cell

        case 4:
            let location = presenter.getLocationDescription()
            let cell = LocationMenuTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(
                delegate: self,
                leftIcon: Asset.pin.image,
                title: "Lokasyon",
                rightIcon: Asset.chevronRight.image,
                descriptionTitle: location.name ?? "",
                description: location.address ?? ""
            )
            return cell
        case 5:
            let cell = TitleButtonAndDescriptionTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      title: "Açıklama",
                      titleFont: presenter.descriptionText.isEmpty ? .body03Compact : .bold03Compact ,
                      buttonTitle: "Düzenle",
                      buttonImage: Asset.pencilEdit.image,
                      isHiddenButton: presenter.descriptionText.isEmpty,
                      description: presenter.descriptionText)
            return cell
        case 6:
            let cell = CheckboxTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.configure(
                delegate: self,
                info: "Bu etkinliği daha sonra kullanmak için şablonlara kaydet!",
                isSelected: presenter.isDraft
            )
            return cell
        default:
            return UITableViewCell()
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        switch indexPath.section {
        case 0:
            return 58
        default:
            return UITableView.automaticDimension
        }
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        switch section {
        case 1:
            return 52
        case 2:
            return 48
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let ckHeaderView = CKHeaderView(textColor: DesignKitColorName.contentStrong900.color,
                                        font: .bold03Compact,
                                        leadingCons: 16,
                                        trailingCons: -16)
        switch section {
        case 1:
            ckHeaderView.updateText("Başlangıç Tarihi ve Saati")
            ckHeaderView.updateImage(Asset.timeBlack.image)
        case 2:
            ckHeaderView.updateText("Etkinlik Türü")
        default:
            let view = UIView()
            view.backgroundColor = DesignKitColorName.contentWeak100.color
            return view
        }
        return ckHeaderView
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        switch section {
        case 0,3,4,5:
            return 1
        case 1,2:
            return 5
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        switch section {
        case 1,2:
            let view = UIView()
            view.backgroundColor = DesignKitColorName.contentWeak100.color
            return view
        default:
            return CKSeparatorView()
        }
    }
}

// MARK: - EventTypeTableViewCellDelegate
extension CalendarAddMissionViewController: EventTypeTableViewCellDelegate {
    func didSelectEventType(with model: EventTypeModel) {
        presenter.didSelectEventType(model)
    }
    
    func addEventType() {
        presenter.addEventType()
    }
}

// MARK: - TextViewTableViewCellDelegate
extension CalendarAddMissionViewController: TextFieldTableViewCellDelegate {
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?) {
        presenter.textFieldDidEndEditing(text, tag: tag)
    }
}

extension CalendarAddMissionViewController: TitleDateAndHourTableViewCellDelegate {
    func didTappedDate(_ type: TitleDateAndHourTableViewCellType) {
        presenter.didTappedDate(type)
    }
    
    func didTappedHour(_ type: TitleDateAndHourTableViewCellType) {
        presenter.didTappedHour(type)
    }
}

extension CalendarAddMissionViewController: TitleButtonAndDescriptionTableViewCellDelegate {
    func didTappedEditDescriptionButton() {
        presenter.didTappedAddDescriptionButton()
    }
}

extension CalendarAddMissionViewController: CommunityTableViewCellDelegate {
    func didTappedCommunityCell() {
        presenter.didTappedCommunity()
    }
}

extension CalendarAddMissionViewController: LocationMenuTableViewCellDelegate {
    func didTappedLocationMenu() {
        presenter.didTappedLocationMenu()
    }
}

extension CalendarAddMissionViewController: InfoSwitchTableViewCellDelegate {
    func didChangeSwitch(isOn: Bool, tag: Int) {
        presenter.didChangeSwitch(isOn: isOn, tag: tag)
    }
}

extension CalendarAddMissionViewController: RepeatTaskTableViewCellDelegate {
    func didTappedSelectRepeatTask(tag: Int) {
        presenter.didTappedSelectRepeatTask(tag: tag)
    }
    
    func didChangeSwitchRepeatTask(isOn: Bool, tag: Int) {
        presenter.didChangeSwitchRepeatTask(isOn: isOn, tag: tag)
    }
    
    func didTappedDateRepeatTaskTableViewCell(_ date: String) {
        presenter.didTappedDate(.end)
    }
}

extension CalendarAddMissionViewController: CheckboxTableViewCellDelegate {
    func selectedCheckboxTableViewCell(isSelected: Bool, tag: Int) {
        presenter.selectedCheckboxTableViewCell(isSelected, tag: tag)
    }
}

extension CalendarAddMissionViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedSubmitButton()
    }
}
